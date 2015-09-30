package org.accenture.product.lemonade.actions;

import java.lang.reflect.Method;
import java.util.List;

import org.accenture.product.lemonade.Launcher;
import org.accenture.product.lemonade.R;
import org.accenture.product.lemonade.settings.SettingsActivity;

import android.content.Intent;
import android.view.Menu;

public class DefaultAction implements LauncherActions.Action {

	private static final String EXTRA_BINDINGVALUE = "DefaultLauncherAction.EXTRA_BINDINGVALUE";

	public static final int ACTION_OPENCLOSE_DRAWER = 1;
	public static final int ACTION_SHOW_NOTIFICATIONS = 2;
	public static final int ACTION_MANAGE_APPS = 3;
	public static final int ACTION_SYSTEM_SETTINGS = 4;

	private final int mBindingValue;
	private final String mName;

	public static void GetActions(List<LauncherActions.Action> list) {
		Launcher launcher = LauncherActions.getInstance().getLauncher();
		if (launcher == null)
			return;
		list.add(getAction(launcher, ACTION_OPENCLOSE_DRAWER));
		list.add(getAction(launcher, ACTION_SHOW_NOTIFICATIONS));
		list.add(getAction(launcher, ACTION_MANAGE_APPS));
		list.add(getAction(launcher, ACTION_SYSTEM_SETTINGS));
	}

	public static DefaultAction getAction(Launcher launcher, int action) {
		final String name;

		switch(action) {
			case ACTION_OPENCLOSE_DRAWER:
				name = launcher.getString(R.string.action_opendrawer); break;
			case ACTION_SHOW_NOTIFICATIONS:
				name = launcher.getString(R.string.menu_notifications); break;
			case ACTION_MANAGE_APPS:
				name = launcher.getString(R.string.menu_manage_apps); break;
			case ACTION_SYSTEM_SETTINGS:
				name = launcher.getString(R.string.menu_settings); break;
			default:
				name = "";
		}
		if (!name.equals(""))
			return new DefaultAction(action, name);
		return null;
	}

	public DefaultAction(int bindingValue, String name) {
		mBindingValue = bindingValue;
		mName = name;
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public void putIntentExtras(Intent intent) {
		intent.putExtra(EXTRA_BINDINGVALUE, mBindingValue);
	}

	@Override
	public boolean runIntent(Intent intent) {
		if (intent.hasExtra(EXTRA_BINDINGVALUE))
		{
			int val = intent.getIntExtra(EXTRA_BINDINGVALUE, 0);
			if (val == mBindingValue) {
				fireHomeBinding(mBindingValue);
				return true;
			}

		}

		return false;
	}

	@Override
	public int getIconResourceId() {
		switch(mBindingValue) {
			case ACTION_MANAGE_APPS:
				return R.drawable.ic_menu_manage;
//			case ACTION_SHOW_NOTIFICATIONS:
//				return R.drawable.ic_menu_notifications;
			case ACTION_SYSTEM_SETTINGS:
				return R.drawable.ic_menu_preferences;
			default:
				return R.drawable.ic_launcher_home;
		}
	}

	public void addToMenu(Menu menu, int group) {
		menu.add(group, Menu.NONE, Menu.NONE, getName())
			.setIntent(LauncherActions.getInstance().getIntentForAction(this))
			.setIcon(getIconResourceId())
			;
	}

	private static void OpenCloseDrawer(Launcher launcher) {
		if (launcher.isAllAppsVisible())
			launcher.closeAllApps(true);
		else
			launcher.showAllApps(true);
	}

	private static void ShowSettings(Launcher launcher) {
		launcher.startActivity(new Intent(launcher, SettingsActivity.class));
	}

	private static void ShowNotifications(Launcher launcher) {
    	try {
            Object service = launcher.getSystemService("statusbar");
            if (service != null) {
                Method expand = service.getClass().getMethod("expand");
                expand.invoke(service);
            }
        } catch (Exception e) {
        }
	}

	private static void ManageApps(Launcher launcher) {
		launcher.startActivity(new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS));
	}

	private static void SystemSettings(Launcher launcher) {
		final Intent settings = new Intent(android.provider.Settings.ACTION_SETTINGS);
        settings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        launcher.startActivity(settings);
	}

	public static void fireHomeBinding(int value) {
		Launcher launcher = LauncherActions.getInstance().getLauncher();
		if (launcher == null)
			return;
		switch(value) {
			case ACTION_OPENCLOSE_DRAWER: OpenCloseDrawer(launcher); break;
			case ACTION_SHOW_NOTIFICATIONS: ShowNotifications(launcher); break;
			case ACTION_MANAGE_APPS: ManageApps(launcher); break;
			case ACTION_SYSTEM_SETTINGS: SystemSettings(launcher); break;
		}
	}
}
