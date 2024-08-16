package middle;

/**
 * Facade for read/write access to the stock list.
 * The actual implementation of this is held on the middle tier.
 * The actual stock list is held in a relational DataBase on the 
 * third tier.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

import catalogue.Product;
import debug.DEBUG;
import remote.RemoteStockRW_I;

import java.rmi.Naming;
import java.rmi.RemoteException;



public class F_StockRW extends F_StockR 
                       implements StockReadWriter
{
  private RemoteStockRW_I aR_StockRW  = null;
  private String          theStockURL = null;

  public F_StockRW( String url )
  {
    super( url );                                   
    theStockURL = url;
  }
  
  private void connect() throws StockException
  {
    try                                            
    {                                               
      aR_StockRW =                                  
       (RemoteStockRW_I) Naming.lookup(theStockURL);
    }
    catch ( Exception e )                           
    {                                                     aR_StockRW = null;
      throw new StockException( "Com: " + 
                               e.getMessage()  );   
      
    }
  }

  public boolean buyStock( String number, int amount )
         throws StockException
  {
    DEBUG.trace("F_StockRW:buyStock()" );
    try
    {
      if ( aR_StockRW == null ) connect();
      return aR_StockRW.buyStock( number, amount );
    } catch ( RemoteException e )
    {
      aR_StockRW = null;
      throw new StockException( "Net: " + e.getMessage() );
    }
  } 
  public void addStock( String number, int amount )
         throws StockException
  {
    DEBUG.trace("F_StockRW:addStock()" );
    try
    {
      if ( aR_StockRW == null ) connect();
      aR_StockRW.addStock( number, amount );
    } catch ( RemoteException e )
    {
      aR_StockRW = null;
      throw new StockException( "Net: " + e.getMessage() );
    }
  }
  public void modifyStock( Product detail )
              throws StockException
  {
    DEBUG.trace("F_StockRW:modifyStock()" );
    try
    {
      if ( aR_StockRW == null ) connect();
      aR_StockRW.modifyStock( detail );
    } catch ( RemoteException e )
    {
      aR_StockRW = null;
      throw new StockException( "Net: " + e.getMessage() );
    }
  }

}