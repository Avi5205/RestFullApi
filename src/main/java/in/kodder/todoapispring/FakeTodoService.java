package in.kodder.todoapispring;

import org.springframework.stereotype.Service;

@Service("fakeTodoService")
public class FakeTodoService implements TodoService {

    @TimeMonitor
    public String doSomething() {
        for (int i = 0; i < 1000000000; i++) {
        }
        // Simulating some work
        return "Doing something in FakeTodoService";


    }
}
