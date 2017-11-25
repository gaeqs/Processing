package com.degoos.processing.engine.object.inheritance;

import com.degoos.processing.engine.object.GObject;
import java.util.List;

public interface Parent {

	List<? extends GObject> getChildren();

}
