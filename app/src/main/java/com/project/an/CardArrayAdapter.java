package com.project.an;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CardArrayAdapter  extends ArrayAdapter<Card> {

    private List<Card> cardList = new ArrayList<Card>();
    // Watch Video activity that contains this adapter
    private WatchVideosActivity wva;

    static class CardViewHolder {
        TextView line1;
        TextView line2;
    }

    public CardArrayAdapter(Context context, int textViewResourceId, WatchVideosActivity wva) {
        super(context, textViewResourceId);
        this.wva = wva;
    }

    @Override
    public void add(Card object) {
        cardList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public Card getItem(int index) {
        return this.cardList.get(index);
    }

    // Setting up video card list view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item_card, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.line1 = (TextView) row.findViewById(R.id.line1);
            viewHolder.line2 = (TextView) row.findViewById(R.id.line2);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder)row.getTag();
        }
        Card card = getItem(position);
        viewHolder.line1.setText(card.getTitle());
        viewHolder.line2.setText(card.getDescription());
        row.setOnClickListener(
                view -> wva.onCardClick(position)
        );
        return row;
    }

}