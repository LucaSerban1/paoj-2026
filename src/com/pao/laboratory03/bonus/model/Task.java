package com.pao.laboratory03.bonus.model;

public class Task {
    private String id;
    private String title;
    private String assignee;
    Status status;
    Priority priority;

    public Task(String id, String title, String assignee, Status status, Priority priority) {
        this.id = id;
        this.title = title;
        this.assignee = assignee;
        this.status = status;
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getTitle() {
        return title;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", priority=" + priority +
                ", status=" + status +
                ", assignee=" + assignee +
                '}';
    }
}
