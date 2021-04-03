package it.units.assistants;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import it.units.utils.MyException;

public class TokenDownloadAssistant {

    private static final String secret = "E}MrcVYG{,S}?m%;8dQ(rn/n27]A!:uz%8axE$9V;D%FcZXXH[LQ9Kgzw2UxMcW%DSNZG6UB;" +
            "hyY6}=)q=p&fVcMWHKZapZu8Kawy{WePRUvV@vFLD,A+#+6im#D,4a8ngV/FDn,Lb]dxwmcxadpM_{pRdygtRbifBaN11GvYlN61Ss" +
            "KTFqbxNWUSEyE06t1i9+KLJfdzCSvcOLpGeyao3vblzUz8qhLpPDbNN5x84KTdNXgAWKhTKbRGCI6vYE+5qkdxgvFg5wquR4vjVah0" +
            "PaTcewIB1WbVaZQMdCT7HxkZQ5RqdjEmbPTwrCh3unDXmrSG+CmdBghpTAydVTJ6gVsSNSP2knwTYbvoRkycYCTGWlQYZVo7JgY70x" +
            "3N)W4Ut-pZ[6Xr(S,68PhUacb*/@T;McuDhF24#Qn:98a!(n9*gvH7ppSeXkK,%-jCf==TZgET*+GN;dkE3i)v:?.k/&.f&Qni:$k}" +
            "H6@]EBN_.R6;4=&8;pa?tA?_kzhvF2!82Mxp&j3{qmnV=-DeB78YmcjFgtqL=)Sq+m]&ck7X-$u%!QKj56Xwn.4372_B[@HzdfAZ88" +
            "NhoTXoEuadb89AgMBAAECggEABchYm7pu9U7roO2sijdLHM4vZn7M/MT+xtJ6wQmIIu7ViFZHxbEXGNFKOi7SfQt3R;?c:GV=NQM[$" +
            "3whyy7+LPAvbAFd+BA+ZT/KoV6zamB6EHAb/Vyy2tQr5k7tCkYdkWOODQfkU6L1o4ZTRghWiWFA0Eudagh7ePi8MQ240ap855TSfSk" +
            "hfup@ne]M2.hPHC{@L,U3CeRE)X*PvM,kC,MpxJd&HC*3bKbaJk2692U5WzltsBoKcqFRkj)qFWmYbXp=Q}v4fR{hBMR,uCD(VTLfU" +
            "L+3kZGjPe8BOkOyLVKpbJqEXwfNHsdnrnrM+RJYjNhXH20JX88wEUJwk/AFmQKBgQDg/oicGTSoBUNJljUn0tP8CJ05WIXDCK6jY1p" +
            "?DgV+4n7#-Wd6r5%w,N:8?@hbW(HYUyPX.=#{3dGKfvnP5FzrF(286GBRX?8Ny.WEt5*,NpdbRta8LcHaEdWNlOlyyLVavYQ0B+SWY" +
            "5HRBDipP3U3Uid6CvQjltBrEyQpO0yCoip1bnBU0jXDS/YP]m?xCb?RGfWV/y$DhCmct4qHa[J}NSku8+i&brd.NR${jb{{79J8VVt" +
            "VTRs8p+piGGv7hB7tkzAIeWtvnx1BrJtvV0LgC3k4Q0hKZ30JAoGBAI85MtfBUjN8JEyBQOXOqtMO5gbb/28RJYPlwd,/$cG}F,_[p" +
            "nxpxfVK6-+.qx/x8T?SH3B!UH-xAFZG6*QGt/CyXwyLwUrKMm!SKpy8C$*ar#-TUHzZ0Ii54bM]9udrGHj_zN]A,=JED4k3(.P:dL6" +
            "L858BH,(ix;/(!JQmdJn5v%7g-mv-:9Wai_hjm(HU8?C@)mhtAQx&uAH#NGi+@W:[dvDx!}h-4Xv,);Ytd6{[}TGx*;RkFeg[@!*3." +
            "]cQ]AE8J&B+X)hD(tWvCdx:!+u7vm[4kS}EeTYa+d)czm5Uex*C(CTYW%!NRd]H..[3R)}*?=E#/$egU$k@V{,B*Tm;ZG/FdCr3[=Y";
    private static final Algorithm algoritmo = Algorithm.HMAC256(secret);

    public static String creaTokenDownload(String fileId) throws MyException {
        try {
            return JWT.create()
                    .withIssuer("Giovanni Marchetto")
                    .withSubject(fileId)
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            System.out.println(exception.getMessage());
            throw new MyException("Eccezione nella creazione del token per il download diretto");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new MyException("Eccezione generica nella creazione del token per il download diretto");
        } catch (NoSuchMethodError e) {
            System.out.println(e.getMessage());
            throw new MyException("Non c'è il metodo per creare il token per il download diretto");
        }
    }

    public static boolean verificaTokenDownload(String token, String fileId) {
        try {
            JWTVerifier verifier = JWT.require(algoritmo)
                    .withIssuer("Giovanni Marchetto")
                    .withSubject(fileId)
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            return jwt != null;
        } catch (JWTVerificationException exception) {
            System.out.println("Invalid signature/claims");
            return false;
        }
    }

}
