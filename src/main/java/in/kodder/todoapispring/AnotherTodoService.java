package in.kodder.todoapispring;

import org.springframework.stereotype.Service;

@Service("anotherTodoService")
public class AnotherTodoService implements TodoService {
    @Override
    public String doSomething() {
        return "Doing something in AnotherTodoService";
    }
}
