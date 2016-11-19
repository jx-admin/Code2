package wu.baselib;

/**
 * Created by jx on 2016/10/25.
 */
public interface IBaseFragmentActivity {
    void onFragmentExit(BaseFragment fragment);

    void onFragmentStartLoading(BaseFragment fragment);

    void onFragmentFinishLoad(BaseFragment fragment);
}
