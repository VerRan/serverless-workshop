package my.service;


import java.util.*;

import my.service.domain.HacksonDomain;
import my.service.domain.HacksonPO;
import my.service.util.*;
import my.service.util.cognito.CognitoHelper;

import static spark.Spark.*;


public class SparkResources {

    public static void defineResources() {
        try {
            String origin="*";
            String header="Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token";
            String methods="GET,OPTIONS";
            enableCORS(origin,methods,header);
            pingPath();
            signUpAndSignIn();
            usermgtApiPath();
            hacksonApiPath();


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void signUpAndSignIn() {
        post("/signup", (req, res) -> {
            String userName = req.queryParams("userName");
            String password = req.queryParams("password");
            String email = req.queryParams("email");

            CognitoHelper cognitoHelper =new CognitoHelper();
            boolean ret = cognitoHelper.SignUpUser(userName,password,
                    email,"");
            if(ret){
                /**register cognito successfull then sycronize the user to db*/
                HacksonPO hacksonPO = new HacksonPO();
                hacksonPO.setUserName(userName);
                hacksonPO.setEmail(email);
                hacksonPO.setPassword(password);
                HacksonDomain.addHackson(hacksonPO);//一张表，写入用户和hackaton使用相同的方法
                res.status(200);
                return "singup successful!";
            }
            res.status(500);
            return "singup error!";

        }, new JsonTransformer());


        post("/verifyaccesscode", (req, res) -> {
            String accessCode = req.queryParams("accessCode");
            String userName = req.queryParams("userName");
            CognitoHelper cognitoHelper =new CognitoHelper();
            boolean ret = cognitoHelper.VerifyAccessCode(userName,accessCode);
            if(ret){
                res.status(200);
                return "signin successful";
            }
            res.status(500);
            return "signin error";
        }, new JsonTransformer());


        post("/signin", (req, res) -> {
            String userName = req.queryParams("userName");
            String password = req.queryParams("password");
            CognitoHelper cognitoHelper =new CognitoHelper();
            String ret = cognitoHelper.ValidateUser(userName,password);
            if(ret!=null){
                res.status(200);
                return ret;
            }
            res.status(500);
            return "signin error";
        }, new JsonTransformer());

    }


    /***ping */
    private static void pingPath() {
        get("/ping", (req, res) -> {
            Map<String, String> pong = new HashMap<>();
            pong.put("pong", "Hello, World!");
            res.status(200);
            return pong;
        }, new JsonTransformer());
    }

    /**user management api */
    private static void usermgtApiPath() {
        path("/api/user",()->{
                    get("/:id", (req, res) -> {
                        String userId = req.params(":id");
                        res.status(200);
                        return HacksonDomain.loadUserInfoByUserId(userId);
                    }, new JsonTransformer());
//
//                    get("/getbyName/:name", (req, res) -> {
//                        String hacksonName = req.params(":name");
//                        System.out.println("hacksonName is "+hacksonName);
//                        res.status(200);
//                        return HacksonDomain.findHacksonByName(hacksonName);
//                    }, new JsonTransformer());


                    post("", (request, response) -> {
                        String userName = request.queryParams("userName");
                        String password = request.queryParams("password");
                        String email = request.queryParams("email");
                        HacksonPO userInfo = new HacksonPO();
                        userInfo.setId(UuidGenerator.getUUID());
                        userInfo.setMetaData("userId-"+ userInfo.getId());
                        userInfo.setUserName(userName);
                        userInfo.setEmail(email);
                        userInfo.setPassword(password);
                        userInfo.setUserCreateDate(DateUtil.getISO8601DateString(new Date()));
                        HacksonDomain.addHackson(userInfo);
                        response.status(200);
                        return "success";
                    }, new JsonTransformer());


//                    put("/:id", (request, response) -> {
//                        String id = request.params(":id");
//                        HacksonPO hacksonPO = HacksonDomain.loadHacksonDeatilByHacksonId(id);
//                        if (hacksonPO != null) {
//                            String hacksonName = request.queryParams("hacksonName");
//                            String descrption = request.queryParams("descrption");
//                            if (hacksonName != null) {
//                                hacksonPO.setHacksonName(hacksonName);
//                            }
//                            if (descrption != null) {
//                                hacksonPO.setDescrption(descrption);
//                            }
//                            HacksonDomain.updateHackson(hacksonPO);
//                            return "hackson with id '" + id + "' updated";
//                        } else {
//                            response.status(404); // 404 Not found
//                            return "Hackson not found";
//                        }
//                    });
//                    // Deletes the book resource for the provided id
//                    delete("/:id", (request, response) -> {
//                        String id = request.params(":id");
//                        HacksonPO hacksonPO = HacksonDomain.deleteHackson(id);
//                        if (hacksonPO != null) {
//                            return "Hackson with id '" + id + "' deleted";
//                        } else {
//                            response.status(404); // 404 Not found
//                            return "Hackson not found";
//                        }
//                    });
                }
        );


    }



    /**hacksonAPI*/
    private static void hacksonApiPath() {

        //Gets all available hackson resources (ids)
        get("/api/hacksons", (request, response) -> {
            response.status(200);
            return HacksonDomain.findAllHackathons();
        },new JsonTransformer());

        path("/api/hackson",()->{
                    get("/:id", (req, res) -> {
                        String hacksonId = req.params(":id");
                        System.out.println("hacksonId is "+hacksonId);
                        res.status(200);
                        return HacksonDomain.loadHacksonDeatilByHacksonId(hacksonId);
                    }, new JsonTransformer());

                    get("/getbyName/:name", (req, res) -> {
                        String hacksonName = req.params(":name");
                        res.status(200);
                        return HacksonDomain.findHacksonByName(hacksonName);
                    }, new JsonTransformer());


                    post("", (req, res) -> {
                        String hacksonName = req.queryParams("hacksonName");
                        String descrption = req.queryParams("descrption");
                        HacksonPO hacksonPO = new HacksonPO();
                        hacksonPO.setId(UuidGenerator.getUUID());
                        hacksonPO.setHacksonName(hacksonName);
                        hacksonPO.setDescrption(descrption);
                        hacksonPO.setMetaData("Details");
                        hacksonPO.setHacksonState("create");
                        hacksonPO.setHacksonCreateDate(DateUtil.getISO8601DateString(new Date()));
                        HacksonDomain.addHackson(hacksonPO);
                        res.status(200); // 200 Created
                        return "success";
                    }, new JsonTransformer());

                    put("/:id", (request, response) -> {
                        String id = request.params(":id");
                        HacksonPO hacksonPO = HacksonDomain.loadHacksonDeatilByHacksonId(id);
                        if (hacksonPO != null) {
                            String hacksonName = request.queryParams("hacksonName");
                            String descrption = request.queryParams("descrption");
                            if (hacksonName != null) {
                                hacksonPO.setHacksonName(hacksonName);
                            }
                            if (descrption != null) {
                                hacksonPO.setDescrption(descrption);
                            }
                            HacksonDomain.updateHackson(hacksonPO);
                            return "hackson with id '" + id + "' updated";
                        } else {
                            response.status(404); // 404 Not found
                            return "Hackson not found";
                        }
                    });
                    // Deletes the book resource for the provided id
                    delete("/:id", (request, response) -> {
                        String id = request.params(":id");
                        HacksonPO hacksonPO = HacksonDomain.deleteHackson(id,"Details");
                        if (hacksonPO != null) {
                            return "Hackson with id '" + id + "' deleted";
                        } else {
                            response.status(404); // 404 Not found
                            return "Hackson not found";
                        }
                    });


                    post("/join", (request, response) -> {
                        String userId = request.queryParams("userId");
                        String id = request.queryParams("id");
                        try {

                           if(HacksonDomain.isAtened(id,userId)){
                               response.status(500); // 404 Not found
                                return "Sorry, the same event can only be attended once ";
                           }

                            HacksonPO hacksonPO = HacksonDomain.loadHacksonDeatilByHacksonId(id);
                            if (hacksonPO != null) {
                                HacksonPO userInfo = HacksonDomain.findUserByUserid(userId);
                                if(userInfo==null){
                                    response.status(404); // 404 Not found
                                    return "can not find the user info by "+userId;
                                }
                                CombineBeans.combineHackson(userInfo, hacksonPO);
                                hacksonPO.setId(id);
                                hacksonPO.setProjectState("create");
                                hacksonPO.setPassword("");
                                hacksonPO.setProjectStartTime(DateUtil.getISO8601DateString(new Date()));
                                HacksonDomain.addHackson(hacksonPO);
                                response.status(200);
                                return "user '"  + hacksonPO.getUserName() + "'  join into hackathon" + hacksonPO.getHacksonName()+ " success!";
                            } else {
                                response.status(404); // 404 Not found
                                return "Hackson not found";
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            response.status(500); // 404 Not found
                            return e.getMessage();
                        }
                    });

                    put("/start/:id", (request, response) -> {
                        String id = request.params(":id");
                        HacksonPO hacksonPO = HacksonDomain.loadHacksonDeatilByHacksonId(id);
                        if (hacksonPO != null) {
                            /*1. update hackson status to start*/
                            hacksonPO.setHacksonState("start");
                            hacksonPO.setHacksonStartTime(DateUtil.getISO8601DateString(new Date()));

                            /*2. update the user project state to start */
                            List<HacksonPO> attendUsers = HacksonDomain.querAttendUsersByHaksonId(id);
                            for(HacksonPO attendUser:attendUsers){
                                if(!attendUser.getMetaData().equals("Details")){// Hackathon Details item not updated
                                    attendUser.setProjectStartTime(DateUtil.getISO8601DateString(new Date()));
                                    attendUser.setProjectState("start");
                                    attendUser.setHacksonStartTime(hacksonPO.getHacksonStartTime());
                                    attendUser.setHacksonState(hacksonPO.getHacksonState());
                                    HacksonDomain.updateHackson(attendUser);
                                }
                            }

                            response.status(200);
                            HacksonDomain.updateHackson(hacksonPO);

                            return "hackson have been  id '" + hacksonPO.getHacksonName() + "' started";
                        } else {
                            response.status(404); // 404 Not found
                            return "Hackson not found";
                        }
                    });



                post("/complete", (request, response) -> {
                    String userId = request.queryParams("userId");
                    String id = request.queryParams("id");
                    HacksonPO hacksonPO = HacksonDomain.queryProjectInfo(id,userId);
                    if (hacksonPO == null) {
                        response.status(404); // 404 Not found
                        return "your hackson not found";
                    } else {
                        hacksonPO.setProjectState("complete");
                        hacksonPO.setProjectEndTime(DateUtil.getISO8601DateString(new Date()));
                        HacksonDomain.updateHackson(hacksonPO);
                        return "your project have completed";
                    }
                });

            post("/exit", (request, response) -> {
                String userId = request.queryParams("userId");
                String id = request.queryParams("id");
                HacksonPO hacksonPO = HacksonDomain.queryProjectInfo(id,userId);
                if (hacksonPO == null) {
                    response.status(404); // 404 Not found
                    return "your hackson not found";
                } else {
                    HacksonDomain.deleteHackson(id,"userId-"+userId);
                    return "you exit hackathon success";
                }
            });

            post("/score", (request, response) -> {
                String userId = request.queryParams("userId");
                String id = request.queryParams("id");
                String score = request.queryParams("score");
                HacksonPO hacksonPO = HacksonDomain.queryProjectInfo(id,userId);
                if (hacksonPO == null) {
                    response.status(404); // 404 Not found
                    return "your hackson not found";
                } else {
                    hacksonPO.setScore(score);
                    HacksonDomain.updateHackson(hacksonPO);
                    return "score success";
                }
            });



                }
        );


    }


    // Enables CORS on requests. This method is an initialization method and should be called once.
    private static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }

}



