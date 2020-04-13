package my.service;


import java.util.HashMap;
import java.util.Map;

import my.service.domain.HacksonDomain;
import my.service.domain.HacksonPO;
import my.service.domain.UserDomain;
import my.service.util.JsonTransformer;
import my.service.util.cognito.CognitoHelper;

import static spark.Spark.*;


public class SparkResources {

    public static void defineResources() {
        try {
            before((request, response) -> response.type("application/json"));
            pingPath();
            usermgtApiPath();
            hacksonApiPath();


        }catch (Exception e){
            e.printStackTrace();
        }
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
                UserDomain.addUser(hacksonPO);
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
                return "signin successful";
            }
            res.status(500);
            return "signin error";
        }, new JsonTransformer());
    }



    /**hacksonAPI*/
    private static void hacksonApiPath() {
        path("/api/hackson",()->{
                    before("/*", (request, response) -> {
                        String password = request.queryParams("token");
                        if (!(password != null)) {
                            halt(401, "You are not welcome here!!!");
                        }
                    });
                    get("/:id", (req, res) -> {
                        String hacksonId = req.params(":id");
                        System.out.println("hacksonId is "+hacksonId);
                        res.status(200);
                        return HacksonDomain.loadHacksonDeatilByHacksonId(hacksonId);
                    }, new JsonTransformer());

                    get("/getbyName/:name", (req, res) -> {
                        String hacksonName = req.params(":name");
                        System.out.println("hacksonName is "+hacksonName);
                        res.status(200);
                        return HacksonDomain.findHacksonByName(hacksonName);
                    }, new JsonTransformer());


                    post("", (req, res) -> {
                        String hacksonName = req.queryParams("hacksonName");
                        String descrption = req.queryParams("descrption");
                        HacksonPO hacksonPO = new HacksonPO();
                        hacksonPO.setHacksonName(hacksonName);
                        hacksonPO.setDescrption(descrption);
                        HacksonDomain.addHackson(hacksonPO);
                        res.status(201); // 201 Created
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
                        HacksonPO hacksonPO = HacksonDomain.deleteHackson(id);
                        if (hacksonPO != null) {
                            return "Hackson with id '" + id + "' deleted";
                        } else {
                            response.status(404); // 404 Not found
                            return "Hackson not found";
                        }
                    });
                }
        );

//        Gets all available book resources (ids)
//        get("/hacksons", (request, response) -> {
//            String ids = "";
//            for (String id : hacksons.keySet()) {
//                ids += id + " ";
//            }
//            return ids;
//        });
    }
}