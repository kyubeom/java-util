
import java.util.Map;
import java.util.stream.Collectors;

public class MapSearcher<T> {

    final private String SEPARATOR = "\\.";
    private Class<T> type;

    private MapSearcher(){ }

    public MapSearcher(Class<T> type){
        this.type = type;
    }

    public T get(Map<String,Object> target, String keyPath){

        if( target == null || target.isEmpty()
            || keyPath == null || "".equals(keyPath.trim())){
            throw new IllegalArgumentException("parameter is empty.");
        }

        String[] splited = keyPath.split(SEPARATOR);
        Map<String,Object> temp = target.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)); //deep copy

        // nested map search
        for(int index = 0; index < splited.length-1; index ++){
            String key = splited[index];
            Object searched = temp.get(key);
            if(searched instanceof Map == false){ // type check
                throw new IllegalArgumentException("path is wrong." +key+ "'s value isn't map type.");
            }
            temp = (Map<String,Object>) searched; // change temp to child map
        }

        // last item check
        int lastIndex = splited.length-1;
        Object searched = temp.get(splited[lastIndex]);
        if(!this.type.isInstance(searched)){
            throw new IllegalArgumentException("type is wrong. " +splited[lastIndex]+ "'s value isn't specified type.");
        }

        return this.type.cast(searched);
    }

    public T getOrDeafult(Map<String,Object> target, String keyPath, T defaultValue){
        try{
            return get(target,  keyPath);
        }catch(Exception e){
            return defaultValue;
        }
    }

}
