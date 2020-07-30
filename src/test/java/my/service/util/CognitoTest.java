package my.service.util;

import my.service.util.cognito.CognitoHelper;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CognitoTest {

    CognitoHelper cognitoHelper =new CognitoHelper();
//    @Test
    public void  testA_cleanData(){
        cognitoHelper.DeleteUser("lht123456");
        System.out.println("ok");
    }

//    @Test
    public void testB_SignUp(){
        boolean ret = cognitoHelper.SignUpUser("lht","Dove1985*",
                "lht@amazon.com","");
        Assert.assertEquals(true,ret);
    }

//    @Test
    public void testC_VerifyCode(){
        CognitoHelper cognitoHelper =new CognitoHelper();
        boolean ret = cognitoHelper.VerifyAccessCode("lht","968904");
        Assert.assertEquals(true,ret);
    }


//    @Test
    public void testD_SignIn(){
        CognitoHelper cognitoHelper =new CognitoHelper();
        String ret = cognitoHelper.ValidateUser("lht","Dove1985*");
        Assert.assertNotNull(ret);
        System.out.println(ret);
    }
}
