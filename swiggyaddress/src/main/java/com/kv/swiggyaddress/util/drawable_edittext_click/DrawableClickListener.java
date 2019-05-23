package com.kv.swiggyaddress.util.drawable_edittext_click;

public interface DrawableClickListener {

        public static enum DrawablePosition { TOP, BOTTOM, LEFT, RIGHT };
        public void onClick(DrawablePosition target);
    }