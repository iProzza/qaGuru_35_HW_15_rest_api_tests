package api.models;

import lombok.Data;

import java.util.List;

@Data
public class UsersListResponse {
    int page;
    int per_page;
    int total;
    int total_pages;
    List<UserResponse> data;

    String name;
    String job;
    String id;
    String createdAt;
    String updatedAt;

    private Support support;

    @Data
    public static class Support {
        private String url;
        private String text;
    }
}
