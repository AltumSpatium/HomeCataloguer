package asm.homecataloguer.annotations;

import asm.homecataloguer.models.ContentType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CatalogFileType
{
	ContentType contentType();
}
