package my.service.domain;


import com.amazonaws.services.dynamodbv2.datamodeling.*;

@DynamoDBTable(tableName = "HackathonTable")
public class HacksonPO {

    // Partition key
    @DynamoDBHashKey(attributeName = "Id")
    private String id;

    @DynamoDBRangeKey(attributeName = "metaData")
    private String metaData;

    @DynamoDBAttribute(attributeName = "hackathonCreateDate")
    private String  hacksonCreateDate;

    @DynamoDBAttribute(attributeName = "hackathonExpireDate")
    private String  hacksonExpireDate;

    @DynamoDBAttribute(attributeName = "hackathonStartTime")
    private String  hacksonStartTime;

    @DynamoDBAttribute(attributeName = "hackathonEndTime")
    private String hacksonEndTime;

    @DynamoDBAttribute(attributeName = "hackathonState")
    private String         hacksonState;

    @DynamoDBAttribute(attributeName = "hackathonName")
    private String  hacksonName;

    @DynamoDBAttribute(attributeName = "descrption")
    private String       descrption;

    @DynamoDBAttribute(attributeName = "userName")
    private String userName;

    @DynamoDBAttribute(attributeName = "email")
    private String         email;

    @DynamoDBAttribute(attributeName = "password")
    private String password;

    @DynamoDBAttribute(attributeName = "sex")
    private String        sex;

    @DynamoDBAttribute(attributeName = "userCreateDate")
    private String userCreateDate;

    @DynamoDBAttribute(attributeName = "role")
    private String     role;

    @DynamoDBAttribute(attributeName = "userState")
    private String userState;

    @DynamoDBAttribute(attributeName = "projectStartTime")
    private String      projectStartTime;

    @DynamoDBAttribute(attributeName = "projectEndTime")
    private String projectEndTime;

    @DynamoDBAttribute(attributeName = "projectState")
    private String     projectState;


    @DynamoDBAttribute(attributeName = "projectScore")
    private String     score;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public String getHacksonCreateDate() {
        return hacksonCreateDate;
    }

    public void setHacksonCreateDate(String hacksonCreateDate) {
        this.hacksonCreateDate = hacksonCreateDate;
    }

    public String getHacksonExpireDate() {
        return hacksonExpireDate;
    }

    public void setHacksonExpireDate(String hacksonExpireDate) {
        this.hacksonExpireDate = hacksonExpireDate;
    }

    public String getHacksonStartTime() {
        return hacksonStartTime;
    }

    public void setHacksonStartTime(String hacksonStartTime) {
        this.hacksonStartTime = hacksonStartTime;
    }

    public String getHacksonEndTime() {
        return hacksonEndTime;
    }

    public void setHacksonEndTime(String hacksonEndTime) {
        this.hacksonEndTime = hacksonEndTime;
    }

    public String getHacksonState() {
        return hacksonState;
    }

    public void setHacksonState(String hacksonState) {
        this.hacksonState = hacksonState;
    }

    public String getHacksonName() {
        return hacksonName;
    }

    public void setHacksonName(String hacksonName) {
        this.hacksonName = hacksonName;
    }

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserCreateDate() {
        return userCreateDate;
    }

    public void setUserCreateDate(String userCreateDate) {
        this.userCreateDate = userCreateDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getProjectStartTime() {
        return projectStartTime;
    }

    public void setProjectStartTime(String projectStartTime) {
        this.projectStartTime = projectStartTime;
    }

    public String getProjectEndTime() {
        return projectEndTime;
    }

    public void setProjectEndTime(String projectEndTime) {
        this.projectEndTime = projectEndTime;
    }

    public String getProjectState() {
        return projectState;
    }

    public void setProjectState(String projectState) {
        this.projectState = projectState;
    }
}
