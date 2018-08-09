package com.androidbugokhttp;

import android.app.Application;
import android.util.Log;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.modules.network.OkHttpClientFactory;
import com.facebook.react.modules.network.OkHttpClientProvider;
import com.facebook.react.modules.network.ReactCookieJarContainer;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class MainApplication extends Application implements ReactApplication {

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    public boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage()
      );
    }

    @Override
    protected String getJSMainModuleName() {
      return "index";
    }
  };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
    OkHttpClientProvider.setOkHttpClientFactory(new OkHttpClientFactory() {
        @Override
        public OkHttpClient createNewNetworkModuleClient() {
            return new OkHttpClient.Builder().cookieJar(new ReactCookieJarContainer()).addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    try {
                        // Doing this is fine according to OkHttp docs
                        // https://github.com/square/okhttp/wiki/Interceptors under "Application interceptors"
                        chain.proceed(chain.request());
                        return chain.proceed(chain.request());
                    } catch (IOException e) {
                        Log.e("NETWORK ERROR", Log.getStackTraceString(e));
                        throw e;
                    }
                }
            }).build();
        }
    });
  }
}
