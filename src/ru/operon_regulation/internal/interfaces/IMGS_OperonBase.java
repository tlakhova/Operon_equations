package ru.operon_regulation.internal.interfaces;

import java.util.List;

public interface IMGS_OperonBase
{
	List<IMGS_Operon> getOperons();
	void addOperon(IMGS_Operon op);
}
