package gm.tieba.tabswitch.hooker.model;

import android.content.Context;
import android.content.res.Resources;

import java.lang.ref.WeakReference;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import gm.tieba.tabswitch.hooker.AutoSign;
import gm.tieba.tabswitch.hooker.ContentFilter;
import gm.tieba.tabswitch.hooker.CreateView;
import gm.tieba.tabswitch.hooker.EyeshieldMode;
import gm.tieba.tabswitch.hooker.FollowFilter;
import gm.tieba.tabswitch.hooker.ForbidGesture;
import gm.tieba.tabswitch.hooker.FrsPageFilter;
import gm.tieba.tabswitch.hooker.FrsTab;
import gm.tieba.tabswitch.hooker.HistoryCache;
import gm.tieba.tabswitch.hooker.HomeRecommend;
import gm.tieba.tabswitch.hooker.NewSub;
import gm.tieba.tabswitch.hooker.OpenSign;
import gm.tieba.tabswitch.hooker.OriginSrc;
import gm.tieba.tabswitch.hooker.PersonalizedFilter;
import gm.tieba.tabswitch.hooker.Purify;
import gm.tieba.tabswitch.hooker.PurifyEnter;
import gm.tieba.tabswitch.hooker.PurifyMy;
import gm.tieba.tabswitch.hooker.RedTip;
import gm.tieba.tabswitch.hooker.SaveImages;
import gm.tieba.tabswitch.hooker.StorageRedirect;
import gm.tieba.tabswitch.hooker.ThreadStore;

public class BaseHooker {
    protected static ClassLoader sClassLoader;
    protected static WeakReference<Context> sContextRef;
    protected static Resources sRes;

    protected BaseHooker() {
    }

    protected BaseHooker(ClassLoader classLoader, Resources res) {
        sClassLoader = classLoader;
        sRes = res;
    }

    public static void init(ClassLoader classLoader, Context context, Resources res, Map.Entry<String, ?> entry) throws Throwable {
        sClassLoader = classLoader;
        sContextRef = new WeakReference<>(context);
        sRes = res;
        switch (entry.getKey()) {
            case "home_recommend":
                if ((Boolean) entry.getValue()) new HomeRecommend().hook();
                break;
            case "enter_forum":
                if (!(Boolean) entry.getValue()) break;
                XposedHelpers.findAndHookMethod("com.baidu.tieba.flutter.base.view.FlutterEnterForumDelegateStatic", classLoader, "createFragmentTabStructure", XC_MethodReplacement.returnConstant(null));
                XposedHelpers.findAndHookMethod("com.baidu.tieba.enterForum.home.EnterForumDelegateStatic", classLoader, "isAvailable", XC_MethodReplacement.returnConstant(false));
                break;
            case "new_category":
                if (!(Boolean) entry.getValue()) break;
                XposedHelpers.findAndHookMethod("com.baidu.tieba.flutter.base.view.FlutterNewCategoryDelegateStatic", classLoader, "isAvailable", XC_MethodReplacement.returnConstant(false));
                break;
            case "my_message":
                if (!(Boolean) entry.getValue()) break;
                XposedHelpers.findAndHookMethod("com.baidu.tieba.imMessageCenter.im.chat.notify.ImMessageCenterDelegateStatic", classLoader, "isAvailable", XC_MethodReplacement.returnConstant(false));
                break;
            case "switch_manager":
                XposedHelpers.findAndHookMethod("com.baidu.adp.lib.featureSwitch.SwitchManager", sClassLoader, "findType", String.class, new XC_MethodHook() {
                    @Override
                    public void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if (Preferences.getStringSet().contains(param.args[0])) {
                            param.setResult(-1);
                        }
                    }
                });
                break;
            case "purify":
                if ((Boolean) entry.getValue()) new Purify().hook();
                break;
            case "purify_enter":
                if ((Boolean) entry.getValue()) new PurifyEnter().hook();
                break;
            case "purify_my":
                if ((Boolean) entry.getValue()) new PurifyMy().hook();
                break;
            case "red_tip":
                if ((Boolean) entry.getValue()) new RedTip().hook();
                break;
            case "follow_filter":
                if ((Boolean) entry.getValue()) new FollowFilter().hook();
                break;
            case "personalized_filter":
                if (Preferences.getString(entry.getKey()) != null) new PersonalizedFilter().hook();
                break;
            case "content_filter":
                if (Preferences.getString(entry.getKey()) != null) new ContentFilter().hook();
                break;
            case "frs_page_filter":
                if (Preferences.getString(entry.getKey()) != null) new FrsPageFilter().hook();
                break;
            case "create_view":
                if ((Boolean) entry.getValue()) new CreateView().hook();
                break;
            case "thread_store":
                if ((Boolean) entry.getValue()) new ThreadStore().hook();
                break;
            case "history_cache":
                if ((Boolean) entry.getValue()) new HistoryCache().hook();
                break;
            case "new_sub":
                if ((Boolean) entry.getValue()) new NewSub().hook();
                break;
            case "save_images":
                if ((Boolean) entry.getValue()) new SaveImages().hook();
                break;
            case "my_attention":
                // if ((Boolean) entry.getValue()) new MyAttention().hook();
                break;
            case "auto_sign":
                if ((Boolean) entry.getValue()) new AutoSign().hook();
                break;
            case "open_sign":
                if ((Boolean) entry.getValue()) new OpenSign().hook();
                break;
            case "origin_src":
                if ((Boolean) entry.getValue()) new OriginSrc().hook();
                break;
            case "storage_redirect":
                if ((Boolean) entry.getValue()) new StorageRedirect().hook();
                break;
            case "forbid_gesture":
                if ((Boolean) entry.getValue()) new ForbidGesture().hook();
                break;
            case "eyeshield_mode":
                if ((Boolean) entry.getValue()) new EyeshieldMode().hook();
                break;
            case "agree_num":
                if (!(Boolean) entry.getValue()) break;
                XposedHelpers.findAndHookMethod("tbclient.Agree$Builder", classLoader, "build", boolean.class, new XC_MethodHook() {
                    @Override
                    public void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedHelpers.setObjectField(param.thisObject, "agree_num", XposedHelpers.getObjectField(param.thisObject, "diff_agree_num"));
                    }
                });
                break;
            case "frs_tab":
                if ((Boolean) entry.getValue()) new FrsTab().hook();
                break;
        }
    }
}