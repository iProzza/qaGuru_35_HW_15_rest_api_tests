package api.models;

import lombok.Data;

@Data
public class CrudUserResponseModel {
    String name, job, id, createdAt, updatedAt;
}
