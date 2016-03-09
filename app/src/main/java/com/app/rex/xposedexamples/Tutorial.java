package com.app.rex.xposedexamples;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by zuoyuxiaofu on 2016/3/6.
 */
public class Tutorial implements IXposedHookLoadPackage {

    public static final String COM_TMALL_WIRELESS_WEBVIEW_VIEW_TMWEB_VIEW_CLIENT = "com.tmall.wireless.webview.view.TMWebViewClient";

    public static final String FILE_NAME = "tmalItemInfo.txt";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        XposedBridge.log("Loaded app: " + loadPackageParam.packageName);
        if ("com.tmall.wireless".equals(loadPackageParam.packageName)) {
            XposedBridge.log("天猫============");

//            getContext(loadPackageParam);

        }


//        hookHttpClient();

//        hookHttpUrlConnection();

        if (!loadPackageParam.packageName.equals("com.tmall.wireless"))
            return;

        hookJson(loadPackageParam);
//        hookUCWebView(loadPackageParam);
    }

    private void hookJson(XC_LoadPackage.LoadPackageParam loadPackageParam) {


        //普通的
//        hookJsonObject(loadPackageParam);

        //反射的
        hookJsonObjectRefection(loadPackageParam);

//        hookJsonArray(loadPackageParam);

    }

    private void hookJsonArray(XC_LoadPackage.LoadPackageParam loadPackageParam) {

        XposedHelpers.findAndHookMethod("com.alibaba.fastjson.JSONObject", loadPackageParam.classLoader, "getJSONArray", String.class
                , new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                // TODO Auto-generated method stub
                log_behavior("Json:getJSONArray ->" + (String) param.args[0]);


            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                log_behavior("After Json:getJSONArray ->" + param.getResult());
            }
        });

    }

    private void hookJsonObjectRefection(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod("com.alibaba.fastjson.JSON", loadPackageParam.classLoader, "parseObject",
                String.class, Type.class, XposedHelpers.findClass("com.alibaba.fastjson.parser.ParserConfig", loadPackageParam.classLoader),
                XposedHelpers.findClass("com.alibaba.fastjson.parser.deserializer.ParseProcess", loadPackageParam.classLoader),
                int.class, Array.newInstance(XposedHelpers.findClass("com.alibaba.fastjson.parser.Feature", loadPackageParam.classLoader), 0).getClass(),
                new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        // TODO Auto-generated method stub
                        log_behavior("Json:parseTObject ->" + ((Type) param.args[1]).toString());
                        if (((Type) param.args[1]).toString().equals("class com.taobao.tao.detail.structure.DetailDescStructureEngine$DataResult")) {
                            log_behavior("中奖啦啦啦啦啦啦啦" + "\n" + (String) param.args[0]);

                            saveAsFile((String) param.args[0], FILE_NAME);

                        }
                        log_behavior("Json:parseTObject ->" + (String) param.args[0]);


                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
//                        log_behavior("After Json:parseTObject ->" + param.getResult().toString());
                    }
                });
    }

    /**
     * 保存到本地文件中
     *
     * @param info
     * @param fileName
     */
    private void saveAsFile(String info, String fileName) {


        try {

            File file = new File(Environment.getExternalStorageDirectory().getPath(), fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(info.getBytes());
            log_behavior("saveAsFile" + file.getCanonicalPath());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void hookJsonObject(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod("com.alibaba.fastjson.JSON", loadPackageParam.classLoader, "parseObject", String.class
                , new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                // TODO Auto-generated method stub
                if ("class com.tmall.wireless.common.configcenter.network.mtop.TMConfigResponse".equals(param.args[0])) {
                    log_behavior("Json:parseObject ->" + "TMConfigResponse");
                    return;
                }
                log_behavior("Json:parseObject ->" + (String) param.args[0]);

            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                log_behavior("Json:parseObject ->" + param.getResult());
            }
        });
    }

    private void hookHttpUrlConnection() {
        XposedHelpers.findAndHookMethod("java.net.URL", ClassLoader.getSystemClassLoader(), "openConnection"
                , new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                // TODO Auto-generated method stub
                URL url = (URL) param.thisObject;
                log_behavior("Connect to URL ->");
                log_behavior("The URL = " + url.toString());


            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
            }
        });

        hookPrintWriter();
    }

    private void hookPrintWriter() {
        XposedHelpers.findAndHookMethod("java.io.PrintWriter", ClassLoader.getSystemClassLoader(), "print", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                String print = (String) param.args[0];
                Pattern pattern = Pattern.compile("(\\w+=.*)");
                Matcher matcher = pattern.matcher(print);
                if (matcher.matches())
                    Log.i("PrintWriter", "data : " + print);
                //Log.d(tag,"A :" + print);
            }

        });
    }

    private void hookUCWebView(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod(
                "com.tmall.wireless.webview.uccore.view.TMUCWebView", loadPackageParam.classLoader, "loadUrl", String.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        // this will be called before the clock was updated by the original method
                        XposedBridge.log("TMhook============");
                        Object[] args = param.args;
                        printArray(args);

                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        // this will be called after the clock was updated by the original method
                        XposedBridge.log("afterTMhook============");
                        param.getResult();
                    }
                });
    }

    private void log_behavior(String info) {
        Log.i("XPOSRED:", info);

    }

    private void hookHttpClient() {

        XposedHelpers.findAndHookMethod("org.apache.http.impl.client.AbstractHttpClient", ClassLoader.getSystemClassLoader(),
                "execute", HttpHost.class, HttpRequest.class, HttpContext.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        log_behavior("Apache Connect to URL ->");
                        HttpHost host = (HttpHost) param.args[0];

                        HttpRequest request = (HttpRequest) param.args[1];
                        if (request instanceof org.apache.http.client.methods.HttpGet) {
                            org.apache.http.client.methods.HttpGet httpGet = (org.apache.http.client.methods.HttpGet) request;
                            log_behavior("HTTP Method : " + httpGet.getMethod());
                            log_behavior("HTTP GET URL : " + httpGet.getURI().toString());
                            Header[] headers = request.getAllHeaders();
                            if (headers != null) {
                                for (int i = 0; i < headers.length; i++) {
                                    log_behavior(headers[i].getName() + ":" + headers[i].getName());
                                }
                            }
                        } else if (request instanceof HttpPost) {
                            HttpPost httpPost = (HttpPost) request;
                            log_behavior("HTTP Method : " + httpPost.getMethod());
                            log_behavior("HTTP URL : " + httpPost.getURI().toString());
                            Header[] headers = request.getAllHeaders();
                            if (headers != null) {
                                for (int i = 0; i < headers.length; i++) {
                                    log_behavior(headers[i].getName() + ":" + headers[i].getValue());
                                }
                            }
                            HttpEntity entity = httpPost.getEntity();
                            String contentType = null;
                            if (entity.getContentType() != null) {
                                contentType = entity.getContentType().getValue();
                                if (URLEncodedUtils.CONTENT_TYPE.equals(contentType)) {

                                    try {
                                        byte[] data = new byte[(int) entity.getContentLength()];
                                        entity.getContent().read(data);
                                        String content = new String(data, HTTP.DEFAULT_CONTENT_CHARSET);
                                        log_behavior("HTTP POST Content : " + content);
                                    } catch (IllegalStateException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                } else if (contentType.startsWith(HTTP.DEFAULT_CONTENT_TYPE)) {
                                    try {
                                        byte[] data = new byte[(int) entity.getContentLength()];
                                        entity.getContent().read(data);
                                        String content = new String(data, contentType.substring(contentType.lastIndexOf("=") + 1));
                                        log_behavior("HTTP POST Content : " + content);
                                    } catch (IllegalStateException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                }
                            } else {
                                byte[] data = new byte[(int) entity.getContentLength()];
                                try {
                                    entity.getContent().read(data);
                                    String content = new String(data, HTTP.DEFAULT_CONTENT_CHARSET);
                                    log_behavior("HTTP POST Content : " + content);
                                } catch (IllegalStateException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }

                        }

                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);

                        super.afterHookedMethod(param);
                        HttpResponse resp = (HttpResponse) param.getResult();
                        if (resp != null) {
                            log_behavior("Status Code = " + resp.getStatusLine().getStatusCode());
                            Header[] headers = resp.getAllHeaders();
                            if (headers != null) {
                                for (int i = 0; i < headers.length; i++) {
                                    log_behavior(headers[i].getName() + ":" + headers[i].getValue());
                                }
                            }

                        }
                    }
                });

    }


    private void printArray(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            XposedBridge.log("param" + args[i]);
        }
    }

    public void getContext(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        Class<?> instrumentation = XposedHelpers.findClass(
                "android.app.Instrumentation", loadPackageParam.classLoader);

        XposedBridge.hookAllMethods(instrumentation, "newActivity", new XC_MethodHook() {


            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                mCurrentActivity[0] = (Activity) param.getResult();

                Log.v("getContext", "Current Activity : " + mCurrentActivity[0].getClass().getName());
            }
        });
    }

    final Context[] mCurrentActivity = new Context[1];
}
