package com.community.hundred.modules.eventbus;

import com.community.hundred.modules.ui.setup.entry.UpdateEntry;

public class UpdateWrap {

    public final UpdateEntry entry;

    private UpdateWrap(UpdateEntry entry) {
        this.entry = entry;
    }

    public static UpdateWrap getInstance(UpdateEntry entry) {
        return new UpdateWrap(entry);
    }
}
