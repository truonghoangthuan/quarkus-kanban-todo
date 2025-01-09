package org.truonghoangthuan;

import java.util.List;
import java.time.LocalDateTime;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

    @Inject
    TaskRepository taskRepository;

    @GET
    public List<Task> getAllTasks() {
        return taskRepository.listAll();
    }

    @POST
    @Transactional
    public Task createTask(Task task) {
        task.setCreationDate(LocalDateTime.now());
        task.setLastChangeDate(LocalDateTime.now());
        taskRepository.persist(task);
        return task;
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Task updateTask(@PathParam("id") Long id, Task task) {
        Task existingTask = taskRepository.findById(id);
        if (existingTask == null) {
            throw new WebApplicationException("Task not found", 404);
        }
        existingTask.setName(task.getName());
        existingTask.setAssignee(task.getAssignee());
        existingTask.setStatus(task.getStatus());
        existingTask.setLastChangeDate(LocalDateTime.now());
        taskRepository.persist(existingTask);
        return existingTask;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void deleteTask(@PathParam("id") Long id) {
        Task task = taskRepository.findById(id);
        if (task == null) {
            throw new WebApplicationException("Task not found", 404);
        }
        taskRepository.delete(task);
    }
}
