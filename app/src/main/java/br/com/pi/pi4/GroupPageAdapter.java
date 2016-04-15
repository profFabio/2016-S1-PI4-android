package br.com.pi.pi4;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import br.com.pi.pi4.entity.ParticipanteGrupo;
import br.com.pi.pi4.fragment.GroupListFragment;

/**
 * Created by fabio-note on 31/03/2016.
 */
public class GroupPageAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> groups = new ArrayList<Fragment>();
    private ArrayList<String> tabNames = new ArrayList<String>();

    public GroupPageAdapter(FragmentManager fm) {
        super(fm);
    }


    public void addItem (String tabName, Fragment f) {
        groups.add(f);
        tabNames.add(tabName);
    }

    public void refreshGrupos (String tabName, ArrayList<ParticipanteGrupo> participantes){
        if (!tabNames.contains(tabName)) {
            GroupListFragment f = GroupListFragment.newInstance(1);
            addItem(tabName,f);
            f.refreshListaParticipantes(participantes);
        } else {
            int position = tabNames.indexOf(tabName);
            GroupListFragment f = (GroupListFragment) groups.get(position);
            f.refreshListaParticipantes(participantes);

        }

    }

    @Override
    public Fragment getItem(int position) {
        return groups.get(position);
    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames.get(position);
    }
}
