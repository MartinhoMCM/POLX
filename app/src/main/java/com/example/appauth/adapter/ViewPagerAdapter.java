package com.example.appauth.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;



public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mlistaFragmentos = new ArrayList<>();
    private List<String> mlistaTitles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) { return mlistaFragmentos.get(position); }

    @Override
    public int getCount() { return mlistaTitles.size(); }

    @Override
    public CharSequence getPageTitle(int position) {
        return mlistaTitles.get(position);
    }

    /**
     *  Metodo addFragmento serve para setar os fragmentos e
     *  para view Pager i os titulos para a tablayout
     *
     * @param fragment intaciad de um fragmento
     * @param titlu titulo do fragmento para as abas
     *
     */

    public  void addFragmentos(Fragment fragment,String titlu){
         mlistaTitles.add(titlu);
        mlistaFragmentos.add(fragment);
    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
