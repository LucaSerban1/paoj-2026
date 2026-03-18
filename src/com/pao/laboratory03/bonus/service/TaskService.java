package com.pao.laboratory03.bonus.service;
import com.pao.laboratory03.bonus.exception.DuplicateTaskException;
import com.pao.laboratory03.bonus.exception.InvalidTransitionException;
import com.pao.laboratory03.bonus.exception.TaskNotFoundException;
import com.pao.laboratory03.bonus.model.Priority;
import com.pao.laboratory03.bonus.model.Status;
import com.pao.laboratory03.bonus.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskService {
    private Map<String, Task> tasksById;
    private Map<Priority, List<Task>> tasksByPriority;
    private List<String> auditLog;
    private static TaskService instance;
    private int NextID;

    public TaskService() {
        this.tasksById = new HashMap<>();
        this.tasksByPriority = new HashMap<>();
        this.auditLog = new ArrayList<>();
        this.NextID = 1;
    }

    public static TaskService getInstance(){
        if(instance == null){
            instance = new TaskService();
        }
        return instance;
    }


    public Task addTask(String title, Priority priority){
        String s = String.format("T%03d", NextID++);

        Task t = new Task(s,title,null, Status.TODO,priority);

        for(Task aux: tasksById.values()){
            if(aux.getTitle().equals(title))
                throw new DuplicateTaskException("Taskul cu titlul " + title + " a fost deja introdus!");
        }

        tasksById.put(s,t);
        tasksByPriority.computeIfAbsent(priority, k -> new ArrayList<>()).add(t);

        auditLog.add("[ADD] " + s + ": " + title + "( " + priority + " )");

        return t;
    }

    public void assignTask(String taskId, String assignee){
        if(tasksById.containsKey(taskId)){
            tasksById.get(taskId).setAssignee(assignee);
            auditLog.add("[ASSIGN] " + taskId  + " -> " + assignee);
        }
        else{
            throw new TaskNotFoundException("Task " + taskId + " does not exist!");
        }
    }

    public void changeStatus(String taskId, Status newStatus){
        if(!tasksById.containsKey(taskId)){
            throw new TaskNotFoundException("Task " + taskId + " does not exist!");
        }
        else{
            Status status = tasksById.get(taskId).getStatus();
            if(!status.canTransitionTo(newStatus))
            {
                throw new InvalidTransitionException(status, newStatus);
            }
            else{
                tasksById.get(taskId).setStatus(newStatus);
                auditLog.add("[STATUS] " + taskId + ": " + status + "→ " + newStatus);
            }
        }
    }

    public List<Task> getTasksByPriority(Priority priority){
        return tasksByPriority.getOrDefault(priority, new ArrayList<>());
    }

    public Map<Status, Long> getStatusSummary(){
        Map<Status,Long> summary = new HashMap<>();
        for(Status s: Status.values()){
            long count = 0;
            for(Task t: tasksById.values()){
                if(s.equals(t.getStatus())) {
                    count++;
                }
            }
            summary.put(s,count);
        }
        return summary;
    }

    public List<Task> getUnassignedTasks(){
        List<Task> unassignedTasks = new ArrayList<>();
        for(Task t: tasksById.values()){
            if(t.getAssignee() == null){
                unassignedTasks.add(t);
            }
        }
        return unassignedTasks;
    }

    public void printAuditLog(){
        for(String s: auditLog){
            System.out.println(s);
        }
    }

    public double getTotalUrgencyScore(int baseDays){
        double sum = 0.0;
        for(Task t: tasksById.values()){
            if(t.getStatus() != Status.DONE && t.getStatus() != Status.CANCELLED){
                sum += t.getPriority().calculateScore(baseDays);
            }
        }
        return sum;
    }
}
