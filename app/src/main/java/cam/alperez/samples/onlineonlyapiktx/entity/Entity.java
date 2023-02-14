package cam.alperez.samples.onlineonlyapiktx.entity;

import cam.alperez.samples.onlineonlyapiktx.utils.IntId;

public interface Entity<T extends Entity<?>> {

    IntId<T> getId();

}
