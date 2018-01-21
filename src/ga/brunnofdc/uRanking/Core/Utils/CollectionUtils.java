package ga.brunnofdc.uRanking.Core.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectionUtils {
	
	@SafeVarargs
	public static <E> List<E> listOf(E... elementos) {
		
        return new ArrayList<>(Arrays.asList(elementos));
        
    }

}
