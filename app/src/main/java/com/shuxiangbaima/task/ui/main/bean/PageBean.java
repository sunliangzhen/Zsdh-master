package com.shuxiangbaima.task.ui.main.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */

public class PageBean {


    public int count;
    public List<TaskListEntity> task_list;

    public int getCount() {
        return count;
    }


    public void setCount(int count) {
        this.count = count;
    }

    public List<TaskListEntity> getTaskList() {
        return task_list;
    }

    public void setTaskList(List<TaskListEntity> task_list) {
        this.task_list = task_list;
    }

    @Override
    public String toString() {
        return "TaskBean{" +
                "count=" + count +
                ", taskList=" + task_list +
                '}';
    }

    public static class TaskListEntity {

        @SerializedName("task_id")
        public String taskId;
        @SerializedName("task_name")
        public String taskName;
        @SerializedName("task_type")
        public String taskType;
        @SerializedName("figure")
        public String figure;
        @SerializedName("profit")
        public double profit;
        @SerializedName("current_status")
        public String currentStatus;
        @SerializedName("left_num")
        public int leftNum;

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public String getTaskType() {
            return taskType;
        }

        public void setTaskType(String taskType) {
            this.taskType = taskType;
        }

        public String getFigure() {
            return figure;
        }

        public void setFigure(String figure) {
            this.figure = figure;
        }

        public double getProfit() {
            return profit;
        }

        public void setProfit(double profit) {
            this.profit = profit;
        }

        public String getCurrentStatus() {
            return currentStatus;
        }

        public void setCurrentStatus(String currentStatus) {
            this.currentStatus = currentStatus;
        }

        public int getLeftNum() {
            return leftNum;
        }

        public void setLeftNum(int leftNum) {
            this.leftNum = leftNum;
        }

        @Override
        public String toString() {
            return "TaskListEntity{" +
                    "taskId='" + taskId + '\'' +
                    ", taskName='" + taskName + '\'' +
                    ", taskType='" + taskType + '\'' +
                    ", figure='" + figure + '\'' +
                    ", profit=" + profit +
                    ", currentStatus='" + currentStatus + '\'' +
                    ", leftNum=" + leftNum +
                    '}';
        }
    }
}
