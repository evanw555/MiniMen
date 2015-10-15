public class BlockData{
	public static final int NUM_IDS = 3;
	public static final int                           AIR = 0, DIRT = 1, WATER_SPRING = 2, WATER = 3, SPROUT = 4, STONE = 5, LADDER = 6, GRAVE = 7;
	public static final boolean[] solid =            {false  , true    , false           , false    , false     , true     , false     , false   };
	private static final boolean[] properties =      {false  , false   , true            , true     , true      , false    , false     , true    };
	private static final boolean[] bottomDependent = {false  , false   , false           , false    , true      , false    , false     , true    };
	private static final boolean[] edgeable =        {false  , true    , false           , false    , false     , false    , false     , false   };
	private static final boolean[] minable =         {false  , true    , false           , false    , false     , false    , false     , false   };
	private static final String[] name =             {"AIR"  , "DIRT"  , "WATER_SPRING"  , "WATER"  , "SPROUT"  , "STONE"  , "LADDER"  , "GRAVE" };
	public static final int NUM_TYPES = solid.length;
	
	public static boolean isWater(int type){
		return type == WATER_SPRING || type == WATER;
	}
	
	public static boolean isSolid(int type){
		return solid[type];
	}
	
	public static boolean hasProperties(int type){
		return properties[type];
	}
	
	public static boolean isBottomDependent(int type){
		return bottomDependent[type];
	}
	
	public static boolean isEdgeable(int type){
		return edgeable[type];
	}
	
	public static boolean isMinable(int type){
		return minable[type];
	}
	
	public static String getName(int type){
		return name[type];
	}
}