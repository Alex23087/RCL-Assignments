import java.util.HashMap;
import java.util.Map;

public class MimeParser {
    public enum MimeType {
        Binary("application/octet-stream"),
        Gif("image/gif"),
        Jpeg("image/jpeg"),
        PlainText("text/plain"),
        Png("image/png"),
        Webp("image/webp");


        private final String type;

        MimeType(String type) {
            this.type = type;
        }

        public String toString(){
            return type;
        }
    }

    private static final Map<String, MimeType> map = new HashMap<>();

    private MimeParser(){}

    static{
        map.put("txt", MimeType.PlainText);
        map.put("jpg", MimeType.Jpeg);
        map.put("jpeg", MimeType.Jpeg);
        map.put("gif", MimeType.Gif);
        map.put("png", MimeType.Png);
        map.put("webp", MimeType.Webp);
    }

    public static MimeType getTypeFromFilename(String filename){
        if(filename.contains(".")){
            return map.getOrDefault(filename.substring(filename.lastIndexOf('.') + 1), MimeType.Binary);
        }else{
            return MimeType.Binary;
        }
    }
}