package in.kodder.todoapispring;

import org.springframework.stereotype.Service;

@Service("fakeTodoService")
public class FakeTodoService implements TodoService {

    @TimeMonitor
    public String doSomething() {
        return "Doing something in FakeTodoService";
    }
}
