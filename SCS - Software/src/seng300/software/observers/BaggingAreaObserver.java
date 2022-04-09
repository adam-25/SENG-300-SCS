package seng300.software.observers;

import java.util.ArrayList;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.ElectronicScaleObserver;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.products.PLUCodedProduct;
import org.lsmr.selfcheckout.products.Product;

import seng300.software.Cart;
import seng300.software.SelfCheckoutSystemLogic;

public class BaggingAreaObserver implements ElectronicScaleObserver
{
	private SelfCheckoutSystemLogic logic;
	private Cart currentCart = new Cart();
	private double weightAtLastEvent;
	private boolean currentItemBagged = true;
	private boolean currentItemRemoved = true;
	
	private boolean baggingItems = true; //false means we are removing items

	private Thread checkProductBagggedby5Thread;

	private Product currentScannedProduct;
	private ArrayList<Product> scannedProducts = new ArrayList<>();
	private ArrayList<Product> baggedProducts = new ArrayList<>();


	private BarcodedProduct currentRemovedProduct; // currentRemovedProduct may be a plu coded

	private boolean timedOut = false;
	
	public boolean isBaggingItems() {
		return baggingItems;
	}

	public void setBaggingItems(boolean baggingItems) {
		this.baggingItems = baggingItems;
	}
	
	public boolean isTimedOut() {
		return timedOut;
	}

	public void setTimedOut(boolean timedOut) {
		this.timedOut = timedOut;
	}

	public ArrayList<Product> getScannedProducts() {
		return scannedProducts;
	}

	public ArrayList<Product> getBaggedProducts() {
		return baggedProducts;
	}

	public BaggingAreaObserver(SelfCheckoutSystemLogic logic)
	{
		this.logic = logic;
		weightAtLastEvent = 0;
	}
	
	@Override
	public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void weightChanged(ElectronicScale scale, double weightInGrams) {
		
		if(weightAtLastEvent < weightInGrams && baggingItems)	
		{
			if(currentItemBagged == true) {
				// there is no scanned item waiting to be bagged so
				blockScs();	
			}else {
				double itemWeight = (weightInGrams - weightAtLastEvent );
				
				weightAtLastEvent = weightInGrams;
				
				double currentItemWeight;
				
				if (currentScannedProduct instanceof BarcodedProduct)
				{
				    currentItemWeight = ((BarcodedProduct)currentScannedProduct).getExpectedWeight();
				}
				else // p instanceof PLUCodedProduct
				{
				    currentItemWeight = currentCart.getPLUWeight(); // Expected weight is the same as the weight on electronic scale
				}
				
				double difference =  Math.abs(currentItemWeight - itemWeight);
				
				//double sensitivity = scale.getSensitivity();
				
				if (difference < 1E-10)  {
					
					baggedProducts.add(currentScannedProduct);
					currentItemBagged = true;
					
					if(weightAtLastEvent <= scale.getWeightLimit()) {
						// if scale is not overloaded enable scanners again 
						unBlocsScs();
					}
					
				}else {
					// unknown item placed in bagging area
					 blockScs();

				}
			}	
			
		} //to test: this else if
		else if (weightAtLastEvent > weightInGrams && !baggingItems) { //customer wants to remove an item from the baggedArea

			if (this.currentItemRemoved == true) {
				// there is no item waiting to be removed
				blockScs();
			}
			else {
				double itemWeight = (weightInGrams - weightAtLastEvent );
				weightAtLastEvent = weightInGrams;
				
				double currentItemWeight;
				
				if (currentScannedProduct instanceof BarcodedProduct)
				{
				    currentItemWeight = ((BarcodedProduct)currentScannedProduct).getExpectedWeight();
				}
				else // p instanceof PLUCodedProduct
				{
				    currentItemWeight = currentCart.getPLUWeight(); // Expected weight is the same as the weight on electronic scale
				}
				
				double difference =  Math.abs(currentItemWeight + itemWeight);
				
				
				if (difference < 1E-10)  {
					removeBarcodedItem();
					currentItemRemoved = true;
					
					unBlocsScs();
					
				}else {
					// unknown item removed from bagging area
					blockScs();

				}
			}
		}
		
		else {
			blockScs();
		}

		
	}

	@Override
	public void overload(ElectronicScale scale) {
		// weight on scale has exceeded limit
		blockScs();

	}

	@Override
	public void outOfOverload(ElectronicScale scale) {
		// TODO Auto-generated method stub

	}
	
	public void notifiedItemAdded(BarcodedProduct scannedProduct)
	{

		// wait 5 seconds -- Threads
		// if not notified weight change, block system
					
		if (checkProductBagggedby5Thread != null && checkProductBagggedby5Thread.isAlive()) {
			checkProductBagggedby5Thread.interrupt();
		}

		if(scannedProduct.getExpectedWeight() > logic.getBaggingAreaSensitivity()) {
			// disable scanners until item placed in bagging area
			blockScs();
			
			currentScannedProduct = scannedProduct;
			scannedProducts.add(scannedProduct);
			currentItemBagged = false;
			
			Runnable  checkProductBaggged = new CheckBaggedProduct(scannedProduct, this);
			checkProductBagggedby5Thread = new Thread(checkProductBaggged);
			checkProductBagggedby5Thread.setDaemon(true);
			checkProductBagggedby5Thread.start();	
			
			
		}else {				
			// does not need to be placed in the bagging area
		}		
	}
	//to test


	public void notifiedPLUCodedItemAdded(PLUCodedProduct scannedPLUProduct, double Weight)
	{

		// wait 5 seconds -- Threads
		// if not notified weight change, block system
					
		if (checkProductBagggedby5Thread != null && checkProductBagggedby5Thread.isAlive()) {
			checkProductBagggedby5Thread.interrupt();
		}

		if( Weight > logic.getBaggingAreaSensitivity()) {
			// disable scanners until item placed in bagging area
			logic.station.mainScanner.disable();
			logic.station.handheldScanner.disable();
			
			currentScannedProduct = scannedPLUProduct;
			scannedProducts.add(scannedPLUProduct);
			currentItemBagged = false;
			
			Runnable  checkProductBaggged = new CheckBaggedProduct(scannedPLUProduct, this);
			checkProductBagggedby5Thread = new Thread(checkProductBaggged);
			checkProductBagggedby5Thread.setDaemon(true);
			checkProductBagggedby5Thread.start();	
			
			
		}else {				
			// if the item weighs less than the scale's sensitivity, it is ignored
			// does not need to be placed in the bagging area
		}		
		
	}


	public void notifiedItemRemoved(BarcodedProduct removedProduct)
	{

		// wait 5 seconds -- Threads
		// if not notified weight change, block system
					
		if (checkProductBagggedby5Thread != null && checkProductBagggedby5Thread.isAlive()) {
			checkProductBagggedby5Thread.interrupt();
		}

		if(removedProduct.getExpectedWeight() > logic.getBaggingAreaSensitivity()) {
			// disable scanners until item placed in bagging area
			logic.station.mainScanner.disable();
			logic.station.handheldScanner.disable();
			
			currentRemovedProduct = removedProduct;
			scannedProducts.remove(removedProduct);
			currentItemRemoved = false;
			
			Runnable  checkProductRemoved = new CheckRemovedProduct(removedProduct, this);
			checkProductBagggedby5Thread = new Thread(checkProductRemoved);
			checkProductBagggedby5Thread.setDaemon(true);
			checkProductBagggedby5Thread.start();	
			
			
		}else {				
			// if the item weighs less than the scale's sensitivity, it is ignored
			// does not need to be placed in the bagging area
		}		
		
	}


	public boolean isCurrentItemBagged() {
		return currentItemBagged;
	}
	
	public boolean isCurrentItemRemoved() {
		return this.currentItemRemoved;
	}

	public void blockScs() {
		logic.weightDiscBlock();
		
	}
	
	public void unBlocsScs() {
		logic.unblock();
	}
	
	
	private void removeBarcodedItem() { //removes currentScannedProduct from list once
		int removeIndex = 0;
		
		if (currentScannedProduct instanceof BarcodedProduct)
		{
			Barcode code = ((BarcodedProduct) currentScannedProduct).getBarcode();
			for (int i = 0; i< this.baggedProducts.size(); i++) {
				if (baggedProducts.get(i) instanceof BarcodedProduct) {
					if (((BarcodedProduct) (baggedProducts.get(i))).getBarcode().equals(code)) {
						removeIndex = i;
						break;
					}
				}
			}
		}

		baggedProducts.remove(removeIndex);
	}
	
	public void noWeightCheck(){
	//Attendant Needs to ensure correct product has been given
		blockScs();
	}
}

