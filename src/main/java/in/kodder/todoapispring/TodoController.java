package in.kodder.todoapispring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {

    private static List<Todo> todoList;
    //    @Autowired
//    @Qualifier("fakeTodoService") // Use "anotherTodoService" to switch to another implementation)
    private TodoService todoService;
    private TodoService todoService2;

    public TodoController(
            @Qualifier("anotherTodoService") TodoService todoService,
            @Qualifier("fakeTodoService") TodoService todoService2) {
        this.todoService = todoService;
        this.todoService2 = todoService2;
        todoList = new ArrayList<>();
        todoList.add(new Todo(1, false, "Learn Spring Boot", 1));
        todoList.add(new Todo(2, true, "Complete Todo API", 1));

    }

    @GetMapping
    public ResponseEntity<List<Todo>> getTodos() {
        System.out.println(this.todoService2.doSomething());
        return ResponseEntity.ok(todoList);
    }

    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Todo> createTodo(@RequestBody Todo newTodo) {
        todoList.add(newTodo);
        return new ResponseEntity<>(newTodo, HttpStatus.CREATED);
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<?> getTodoById(@PathVariable Long todoId) {
        for (Todo todo : todoList) {
            if (todo.getId() == todoId) {
                return ResponseEntity.ok(todo);
            }
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "Todo with id " + todoId + " not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Add these methods inside TodoController

    @DeleteMapping("/{todoId}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long todoId) {
        for (Todo todo : todoList) {
            if (todo.getId() == todoId) {
                todoList.remove(todo);
                return ResponseEntity.noContent().build();
            }
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "Todo with id " + todoId + " not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Updated PATCH method
    @PatchMapping("/{todoId}")
    public ResponseEntity<?> updateTodo(@PathVariable Long todoId, @RequestBody String rawJson) {
        // Parse JSON manually
        Map<String, Object> updates;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            updates = objectMapper.readValue(rawJson, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid JSON format");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Find todo by ID
        Todo foundTodo = null;
        for (Todo todo : todoList) {
            if (todo.getId() == todoId) {
                foundTodo = todo;
                break;
            }
        }

        if (foundTodo == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Todo with id " + todoId + " not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        // Apply updates
        if (updates.containsKey("completed")) {
            Object completedObj = updates.get("completed");
            if (completedObj instanceof Boolean) {
                foundTodo.setCompleted((Boolean) completedObj);
            }
        }
        if (updates.containsKey("title")) {
            Object titleObj = updates.get("title");
            if (titleObj instanceof String) {
                foundTodo.setTitle((String) titleObj);
            }
        }
        if (updates.containsKey("userId")) {
            Object userIdObj = updates.get("userId");
            if (userIdObj instanceof Number) {
                foundTodo.setUserId(((Number) userIdObj).intValue());
            }
        }

        return ResponseEntity.ok(foundTodo);
    }
}
