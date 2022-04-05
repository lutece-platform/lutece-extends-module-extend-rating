package fr.paris.lutece.plugins.extend.modules.rating.business;



public class RatingExtenderFilter {

	public static final String ALL_STRING = "all";
	public static final float ALL_FLOAT = -99999.9999999f;
	// Variables declarations 
    private float _fRatingValue = ALL_FLOAT ;
    private String _strRatingType;
    private String _strIdExtendableResource;
    private String _strExtendableResourceType;
    private String _strUserGuid;

    
    /**
     * Returns the RatingValue
     * @return The RatingValue
     */ 
     public float getRatingValue()
     {
         return _fRatingValue;
     }
   /**
    * Return true if filter contain rating value
    * 
    */ 
    public boolean containsRatingValue()
    {
        return ALL_FLOAT != _fRatingValue;
    }

   /**
    * Sets the RatingValue
    * @param fRatingValue The RatingValue
    */ 
    public void setRatingValue( float fRatingValue )
    {
        _fRatingValue = fRatingValue;
    }

    /**
     * Returns the RatingType
     * @return The RatingType
     */ 
     public String getRatingType()
     {
         return _strRatingType;
     }
   /**
    *  Return true if filter contain rating type
    * 
    */ 
    public boolean containsRatingType()
    {
        return _strRatingType != null;
    }

   /**
    * Sets the RatingType
    * @param strRatingType The RatingType
    */ 
    public void setRatingType( String strRatingType )
    {
        _strRatingType = strRatingType;
    }

    
     /**
      * Returns the IdExtendableResource
      * @return The IdExtendableResource
      */ 
      public String getIdExtendableResource()
      {
          return _strIdExtendableResource;
      }

   /**
    * Return true if filter contain rating Extendable Resource
    * 
    */ 
    public boolean containsIdExtendableResource()
    {
        return _strIdExtendableResource != null ;
    }

   /**
    * Sets the ExtendableResource
    * @param strExtendableResource The ExtendableResource
    */ 
    public void setIdExtendableResource( String strExtendableResource )
    {
        _strIdExtendableResource = strExtendableResource;
    }

    /**
     * Returns the ExtendableResourceType
     * @return The ExtendableResourceType
     */ 
     public String getExtendableResourceType()
     {
         return _strExtendableResourceType;
     }
   /**
    * Return true if filter contain rating Extendable Resource Type
    * 
    */ 
    public boolean containsExtendableResourceType()
    {
        return _strExtendableResourceType != null;
    }

   /**
    * Sets the ExtendableResourceType
    * @param strExtendableResourceType The ExtendableResourceType
    */ 
    public void setExtendableResourceType( String strExtendableResourceType )
    {
        _strExtendableResourceType = strExtendableResourceType;
    }

   /**
    * Return true if filter contain rating user guid
    * 
    */ 
    public boolean containsUserGuid()
    {
        return _strUserGuid != null ;
    }

   /**
    * Sets the UserGuid
    * @param strEuerGuid The UserGuid
    */ 
    public void setUserGuid( String strUserGuid )
    {
        _strUserGuid = strUserGuid;
    }
    /**
     * Returns the UserGuid
     * @return The UserGuid
     */ 
     public String getUserGuid()
     {
         return _strUserGuid;
     }
}
