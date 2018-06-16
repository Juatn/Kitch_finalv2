package moreno.juan.kitch.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import moreno.juan.kitch.R;


public class GalleryFragment extends Fragment {


    String[] tituloTabs = {"RECETAS", "CATEGORIAS"};
    private AppBarLayout appBar;
    private TabLayout tabs;
    private ViewPager viewPager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        View contenedor = (View) container.getParent();
        appBar = contenedor.findViewById(R.id.appbar);
        tabs = new TabLayout(Objects.requireNonNull(getActivity()));


        tabs.setTabTextColors(Color.parseColor("#D78E7D"), Color.parseColor("#D78E7D"));

        appBar.addView(tabs);
        viewPager = view.findViewById(R.id.pager);
        ViewPageAdapter pageAdapter = new ViewPageAdapter(getFragmentManager());
        viewPager.setAdapter(pageAdapter);
        tabs.setupWithViewPager(viewPager);


        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appBar.removeView(tabs);
    }

    public class ViewPageAdapter extends FragmentStatePagerAdapter {

        public ViewPageAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new Tab_RecetasFragment();
                case 1:
                    return new Tab_CategoriasFragment();
                //case 2: return new Tab_PruebaFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tituloTabs[position];
        }

    }

}
