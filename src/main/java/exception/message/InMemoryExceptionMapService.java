package exception.message;

import base.annotation.Default;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Default
public class InMemoryExceptionMapService implements ExceptionMapService {

    private static Map<Integer, String> stringMap = new HashMap<>();

    public InMemoryExceptionMapService() {
        init();
    }


    @Override
    public Boolean addMessage(int code, String msg) {
        if (stringMap.containsKey(code))
            return false;
        stringMap.put(code, msg);
        return true;
    }

    @Override
    public void init() {
        addMessage(100010, "تامین کننده مالی باید مشخص شود");




    }
}
