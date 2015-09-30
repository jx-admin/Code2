/*

Copyright (C) 2010 Hans-Werner Hilse <hilse@web.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

*/

#include <jni.h>

#include <android/log.h>

#include <errno.h>

#include <fitz.h>
#include <mupdf.h>


/************************************************************************/
/* Macros: */

/* Bytes per pixel */

#define BYPP 4

/* Debugging helper */

#ifdef PDFRENDER_DEBUG
#define DEBUG(args...) \
	__android_log_print(ANDROID_LOG_DEBUG, "PdfRender", args)
#define INFO(args...) \
	__android_log_print(ANDROID_LOG_INFO, "PdfRender", args)
#else
#define DEBUG(args...) {}
#define INFO(args...) {}
#endif

#define ERROR(args...) \
	__android_log_print(ANDROID_LOG_ERROR, "PdfRender", args)


/* Exception classes */

#define EXC						"java/lang/Exception"
#define EXC_CANNOT_REPAIR		"de/hilses/droidreader/CannotRepairException"
#define EXC_CANNOT_DECRYPTXREF	"de/hilses/droidreader/CannotDecryptXrefException"
#define EXC_NEED_PASSWORD		"de/hilses/droidreader/PasswordNeededException"
#define EXC_PAGELOAD			"de/hilses/droidreader/PageLoadException"
#define EXC_PAGERENDER			"de/hilses/droidreader/PageRenderException"
#define EXC_WRONG_PASSWORD		"de/hilses/droidreader/WrongPasswordException"


extern fz_error rendernode(fz_renderer *gc, fz_node *node, fz_matrix ctm);

/************************************************************************/

typedef struct renderdocument_s renderdocument_t;
struct renderdocument_s
{
	pdf_xref *xref;
	pdf_outline *outline;
	fz_renderer *rast;
};

typedef struct renderpage_s renderpage_t;
struct renderpage_s
{
	pdf_page *page;
	fz_matrix ctm;
	fz_rect bbox;
};


/**
 * we cache a reference to the JVM here
 */
JavaVM *cached_jvm;

/************************************************************************/

/* our own helper functions: */

void throw_exception(JNIEnv *env, char *exception_class, char *message)
{
	jthrowable new_exception = (*env)->FindClass(env, exception_class);
	if(new_exception == NULL) {
		ERROR("cannot create Exception '%s', Message was '%s'",
				exception_class, message);
		return;
	} else {
		DEBUG("Exception '%s', Message: '%s'", exception_class, message);
	}
	(*env)->ThrowNew(env, new_exception, message);
}

/* a callback to retrieve font file names */

fz_error
pdf_getfontfile(pdf_fontdesc *font, char *fontname, char *collection, char **filename)
{
	JNIEnv *env;
	jboolean iscopy;
	jclass pdfrender;
	jclass fontproviderclass;
	jfieldID fontproviderfield;
	jobject fontprovider;
	jmethodID getfontfilemethod;
	jstring fontfilestring;
	jstring fontnamestring;
	jstring collectionstring;
	char *filenamebuf;

	DEBUG("pdf_getfontfile(%p, '%s', '%s')", font, fontname, collection);

	if((*cached_jvm)->GetEnv(cached_jvm, (void **)&env, JNI_VERSION_1_2) != JNI_OK)
		return fz_throw("cannot find our JNI env!");

	pdfrender = (*env)->FindClass(env, "de/hilses/droidreader/PdfRender");
	if(pdfrender == NULL)
		return fz_throw("cannot find JNI interface class");

	fontproviderfield = (*env)->GetStaticFieldID(env, pdfrender,
			"fontProvider", "Lde/hilses/droidreader/FontProvider;");
	if(fontproviderfield == NULL)
		return fz_throw("cannot find fontProvider field");

	fontprovider = (*env)->GetStaticObjectField(env, pdfrender, fontproviderfield);
	if(fontprovider == NULL)
		return fz_throw("cannot access fontProvider field");

	fontproviderclass = (*env)->GetObjectClass(env, fontprovider);
	if(fontproviderclass == NULL)
		return fz_throw("cannot get class for fontProvider field content");

	getfontfilemethod = (*env)->GetMethodID(env, fontproviderclass,
			"getFontFile",
			"(Ljava/lang/String;Ljava/lang/String;I)Ljava/lang/String;");
	if(getfontfilemethod == NULL)
		return fz_throw("cannot find method getFontFile() in fontProvider");

	fontnamestring = (*env)->NewStringUTF(env, fontname);
	collectionstring = (*env)->NewStringUTF(env, collection);

	fontfilestring = (*env)->CallObjectMethod(
			env, fontprovider, getfontfilemethod,
			fontnamestring,
			collectionstring,
			(jint) font->flags);

	/* TODO: release some references?!?
	(*env)->DeleteLocalRef(env, fontnamestring);
	(*env)->DeleteLocalRef(env, collectionstring);
	*/

	if(fontfilestring == NULL)
		return fz_throw("could not get filename for font");

	filenamebuf = (*env)->GetStringUTFChars(env, fontfilestring, &iscopy);
	*filename = fz_strdup(filenamebuf);
	(*env)->ReleaseStringUTFChars(env, fontfilestring, filenamebuf);

	DEBUG("got font file: '%s'", *filename);
	return fz_okay;
}

fz_error
pdf_getfontbuffer(pdf_fontdesc *font, char *fontname, char *collection, unsigned char **data, unsigned int *len) {
	JNIEnv *env;
	jboolean iscopy;
	jclass pdfrender;
	jclass fontproviderclass;
	jfieldID fontproviderfield;
	jobject fontprovider;
	jmethodID getfontbuffermethod;
	jobject fontbuffer;
	jstring fontnamestring;
	jstring collectionstring;

	DEBUG("pdf_getfontbuffer(%p, '%s', '%s')", font, fontname, collection);

	if((*cached_jvm)->GetEnv(cached_jvm, (void **)&env, JNI_VERSION_1_2) != JNI_OK)
		return fz_throw("cannot find our JNI env!");

	pdfrender = (*env)->FindClass(env, "de/hilses/droidreader/PdfRender");
	if(pdfrender == NULL)
		return fz_throw("cannot find JNI interface class");

	fontproviderfield = (*env)->GetStaticFieldID(env, pdfrender,
			"fontProvider", "Lde/hilses/droidreader/FontProvider;");
	if(fontproviderfield == NULL)
		return fz_throw("cannot find fontProvider field");

	fontprovider = (*env)->GetStaticObjectField(env, pdfrender, fontproviderfield);
	if(fontprovider == NULL)
		return fz_throw("cannot access fontProvider field");

	fontproviderclass = (*env)->GetObjectClass(env, fontprovider);
	if(fontproviderclass == NULL)
		return fz_throw("cannot get class for fontProvider field content");

	getfontbuffermethod = (*env)->GetMethodID(env, fontproviderclass,
			"getFontBuffer",
			"(Ljava/lang/String;Ljava/lang/String;I)Ljava/nio/ByteBuffer;");
	if(getfontbuffermethod == NULL)
		return fz_throw("cannot find method getFontBuffer() in fontProvider");

	fontnamestring = (*env)->NewStringUTF(env, fontname);
	collectionstring = (*env)->NewStringUTF(env, collection);

	fontbuffer = (*env)->CallObjectMethod(
			env, fontprovider, getfontbuffermethod,
			fontnamestring,
			collectionstring,
			(jint) font->flags);
/* TODO: release some references?!?
	(*env)->DeleteLocalRef(env, fontnamestring);
	(*env)->DeleteLocalRef(env, collectionstring);
*/
	DEBUG("got font buffer: %d", fontbuffer);

	if(fontbuffer == NULL)
		return fz_throw("could not get buffer for font");

	*data = (unsigned char *) (*env)->GetDirectBufferAddress(env, fontbuffer);
	*len = (unsigned int) (*env)->GetDirectBufferCapacity(env, fontbuffer);

	if((*data == NULL) || (*len == -1))
		return fz_throw("could not get buffer for font (JNI trouble!)");

	DEBUG("got font buffer: %p, length=%d", *data, *len);
	return fz_okay;
}

fz_error
pdf_getcmapbuffer(char *cmapname, unsigned char **data, unsigned int *len) {
	JNIEnv *env;
	jboolean iscopy;
	jclass pdfrender;
	jclass fontproviderclass;
	jfieldID fontproviderfield;
	jobject fontprovider;
	jmethodID getcmapbuffermethod;
	jobject cmapbuffer;
	jstring cmapnamestring;

	DEBUG("pdf_getcmapbuffer('%s')", cmapname);

	if((*cached_jvm)->GetEnv(cached_jvm, (void **)&env, JNI_VERSION_1_2) != JNI_OK)
		return fz_throw("cannot find our JNI env!");

	pdfrender = (*env)->FindClass(env, "de/hilses/droidreader/PdfRender");
	if(pdfrender == NULL)
		return fz_throw("cannot find JNI interface class");

	fontproviderfield = (*env)->GetStaticFieldID(env, pdfrender,
			"fontProvider", "Lde/hilses/droidreader/FontProvider;");
	if(fontproviderfield == NULL)
		return fz_throw("cannot find fontProvider field");

	fontprovider = (*env)->GetStaticObjectField(env, pdfrender, fontproviderfield);
	if(fontprovider == NULL)
		return fz_throw("cannot access fontProvider field");

	fontproviderclass = (*env)->GetObjectClass(env, fontprovider);
	if(fontproviderclass == NULL)
		return fz_throw("cannot get class for fontProvider field content");

	getcmapbuffermethod = (*env)->GetMethodID(env, fontproviderclass,
			"getCMapBuffer",
			"(Ljava/lang/String;)Ljava/nio/ByteBuffer;");
	if(getcmapbuffermethod == NULL)
		return fz_throw("cannot find method getCMapBuffer() in fontProvider");

	cmapnamestring = (*env)->NewStringUTF(env, cmapname);

	cmapbuffer = (*env)->CallObjectMethod(
			env, fontprovider, getcmapbuffermethod,
			cmapnamestring);
/* TODO: release some references?!?
	(*env)->DeleteLocalRef(env, cmapnamestring);
*/
	DEBUG("got cmap buffer: %d", cmapbuffer);

	if(cmapbuffer == NULL)
		return fz_throw("could not get buffer for cmap");

	*data = (unsigned char *) (*env)->GetDirectBufferAddress(env, cmapbuffer);
	*len = (unsigned int) (*env)->GetDirectBufferCapacity(env, cmapbuffer);

	if((*data == NULL) || (*len == -1))
		return fz_throw("could not get buffer for cmap (JNI trouble!)");

	DEBUG("got cmap buffer: %p, length=%d (%d)", *data, *len, strlen(*data));
	return fz_okay;
}

/* JNI Interface: */

JNI_OnLoad(JavaVM *jvm, void *reserved)
{
	DEBUG("initializing PdfRender JNI library based on MuPDF");

	/* Fitz library setup */
	fz_cpudetect();
	fz_accelerate();

	cached_jvm = jvm;

	return JNI_VERSION_1_2;
}

JNIEXPORT jint JNICALL
	Java_de_hilses_droidreader_PdfRender_checkFont
	(JNIEnv *env, jobject class, jstring fname)
{
	char *filename;
	jboolean iscopy;
	int result = 1;
	FILE *fd;

	filename = (*env)->GetStringUTFChars(env, fname, &iscopy);

	fd = fopen(filename, "r");
	if(fd) {
		fclose(fd);
		result = 0;
	} else {
		result = errno;
	}

	(*env)->ReleaseStringUTFChars(env, fname, filename);
	return (jint) result;
}

JNIEXPORT jlong JNICALL
	Java_de_hilses_droidreader_PdfDocument_nativeOpen
	(JNIEnv *env, jobject this,
			jint fitzmemory, jstring fname, jstring pwd)
{
	DEBUG("PdfDocument(%p).nativeOpen(%i, \"%p\", \"%p\")",
			this, fitzmemory, fname, pwd);

	fz_error error;
	fz_obj *obj;
	renderdocument_t *doc;
	jboolean iscopy;
	jclass cls;
	jfieldID fid;
	char *filename;
	char *password;

	filename = (*env)->GetStringUTFChars(env, fname, &iscopy);
	password = (*env)->GetStringUTFChars(env, pwd, &iscopy);

	doc = fz_malloc(sizeof(renderdocument_t));
	if(!doc) {
		throw_exception(env, EXC, "Out of Memory");
		goto cleanup;
	}

	/* initialize renderer */

	error = fz_newrenderer(&doc->rast, pdf_devicergb, 0, (int) fitzmemory);
	if (error) {
		throw_exception(env, EXC, "Cannot create new renderer");
		goto cleanup;
	}

	/*
	 * Open PDF and load xref table
	 */

	doc->xref = pdf_newxref();
	error = pdf_loadxref(doc->xref, filename);
	if (error) {
		/* TODO: plug into fitz error handling */
		fz_catch(error, "trying to repair");
		INFO("Corrupted file '%s', trying to repair", filename);
		error = pdf_repairxref(doc->xref, filename);
		if (error) {
			throw_exception(env, EXC_CANNOT_REPAIR,
					"PDF file is corrupted");
			goto cleanup;
		}
	}

	error = pdf_decryptxref(doc->xref);
	if (error) {
		throw_exception(env, EXC_CANNOT_DECRYPTXREF,
				"Cannot decrypt XRef table");
		goto cleanup;
	}

	/*
	 * Handle encrypted PDF files
	 */

	if (pdf_needspassword(doc->xref)) {
		if(strlen(password)) {
			int ok = pdf_authenticatepassword(doc->xref, password);
			if(!ok) {
				throw_exception(env, EXC_WRONG_PASSWORD,
						"Wrong password given");
				goto cleanup;
			}
		} else {
			throw_exception(env, EXC_NEED_PASSWORD,
					"PDF needs a password!");
			goto cleanup;
		}
	}

	/*
	 * Load document metadata (at some point this might be implemented
	 * in the muPDF lib itself)
	 */

	obj = fz_dictgets(doc->xref->trailer, "Root");
	doc->xref->root = fz_resolveindirect(obj);
	if (!doc->xref->root) {
		fz_throw("syntaxerror: missing Root object");
		throw_exception(env, EXC_CANNOT_DECRYPTXREF, "PDF syntax: missing \"Root\" object");
		goto cleanup;
	}
	fz_keepobj(doc->xref->root);

	obj = fz_dictgets(doc->xref->trailer, "Info");
	doc->xref->info = fz_resolveindirect(obj);
	if (doc->xref->info)
		fz_keepobj(doc->xref->info);

	cls = (*env)->GetObjectClass(env, this);

	doc->outline = pdf_loadoutline(doc->xref);
	/* TODO: passing outline to Java env or create accessor functions */

	if (doc->xref->info) {
		obj = fz_dictgets(doc->xref->info, "Title");
		if (obj) {
			fid = (*env)->GetFieldID(env, cls, "metaTitle",
					"Ljava/lang/String;");
			if(fid) {
				jstring jstr = (*env)->NewStringUTF(env, pdf_toutf8(obj));
				(*env)->SetObjectField(env, this, fid, jstr);
			}
		}
	}

	fid = (*env)->GetFieldID(env, cls, "pagecount","I");
	if(fid) {
		(*env)->SetIntField(env, this, fid,
				pdf_getpagecount(doc->xref));
	} else {
		throw_exception(env, EXC, "cannot access instance fields!");
	}

cleanup:

	(*env)->ReleaseStringUTFChars(env, fname, filename);
	(*env)->ReleaseStringUTFChars(env, pwd, password);

	DEBUG("PdfDocument.nativeOpen(): return handle = %p", doc);
	return (jlong) doc;
}

JNIEXPORT void JNICALL
	Java_de_hilses_droidreader_PdfDocument_nativeClose
	(JNIEnv *env, jobject this, jlong handle)
{
	renderdocument_t *doc = (renderdocument_t*) handle;
	DEBUG("PdfDocument(%p).nativeClose(%p)", this, doc);

	if(doc) {
		if (doc->outline)
			pdf_dropoutline(doc->outline);

		if (doc->xref->store)
			pdf_dropstore(doc->xref->store);

		pdf_closexref(doc->xref);

		fz_droprenderer(doc->rast);

		fz_free(doc);
	}
}

JNIEXPORT jlong JNICALL
	Java_de_hilses_droidreader_PdfPage_nativeOpenPage
	(JNIEnv *env, jobject this, jlong dochandle, jfloatArray mediabox, jint pageno)
{
	renderdocument_t *doc = (renderdocument_t*) dochandle;
	DEBUG("PdfPage(%p).nativeOpenPage(%p)", this, doc);
	renderpage_t *page;
	fz_error error;
	fz_obj *obj;
	jclass cls;
	jfieldID fid;

	page = fz_malloc(sizeof(renderpage_t));
	if(!page) {
		throw_exception(env, EXC, "Out of Memory");
		return (jlong) NULL;
	}

	pdf_flushxref(doc->xref, 0);
	obj = pdf_getpageobject(doc->xref, pageno);
	error = pdf_loadpage(&page->page, doc->xref, obj);
	if (error) {
		throw_exception(env, EXC_PAGELOAD, "error loading page");
		goto cleanup;
	}

	jfloat *bbox = (*env)->GetPrimitiveArrayCritical(env, mediabox, 0);
	if(bbox == NULL) {
		throw_exception(env, EXC, "out of memory");
		goto cleanup;
	}
	bbox[0] = page->page->mediabox.x0;
	bbox[1] = page->page->mediabox.y0;
	bbox[2] = page->page->mediabox.x1;
	bbox[3] = page->page->mediabox.y1;
	(*env)->ReleasePrimitiveArrayCritical(env, mediabox, bbox, 0);

	cls = (*env)->GetObjectClass(env, this);
	fid = (*env)->GetFieldID(env, cls, "rotate","I");
	if(fid) {
		(*env)->SetIntField(env, this, fid, page->page->rotate);
	} else {
		throw_exception(env, EXC, "cannot access instance fields!");
	}

cleanup:
	/* nothing yet */

	DEBUG("PdfPage.nativeOpenPage(): return handle = %p", page);
	return (jlong) page;
}

JNIEXPORT void JNICALL
	Java_de_hilses_droidreader_PdfPage_nativeClosePage
	(JNIEnv *env, jobject this, jlong handle)
{
	renderpage_t *page = (renderpage_t*) handle;
	DEBUG("PdfPage(%p).nativeClosePage(%p)", this, page);
	if(page) {
		if (page->page)
			pdf_droppage(page->page);

		fz_free(page);
	}
}

JNIEXPORT void JNICALL
	Java_de_hilses_droidreader_PdfView_nativeCreateView
	(JNIEnv *env, jobject this, jlong dochandle, jlong pagehandle,
		jintArray viewboxarray, jfloatArray matrixarray,
		jintArray bufferarray)
{
	renderdocument_t *doc = (renderdocument_t*) dochandle;
	renderpage_t *page = (renderpage_t*) pagehandle;
	DEBUG("PdfView(%p).nativeCreateView(%p, %p)", this, doc, page);
	fz_error error;
	fz_matrix ctm;
	fz_irect viewbox;
	fz_pixmap *pixmap;
	jfloat *matrix;
	jint *viewboxarr;
	jint *dimen;
	jint *buffer;
	int length, val;

	pixmap = fz_malloc(sizeof(fz_pixmap));
	if(!pixmap) {
		throw_exception(env, EXC, "Out of Memory");
	}

	/* initialize parameter arrays for MuPDF */

	matrix = (*env)->GetPrimitiveArrayCritical(env, matrixarray, 0);
	ctm.a = matrix[0];
	ctm.b = matrix[1];
	ctm.c = matrix[2];
	ctm.d = matrix[3];
	ctm.e = matrix[4];
	ctm.f = matrix[5];
	(*env)->ReleasePrimitiveArrayCritical(env, matrixarray, matrix, 0);
	DEBUG("Matrix: %f %f %f %f %f %f",
			ctm.a, ctm.b, ctm.c, ctm.d, ctm.e, ctm.f);

	viewboxarr = (*env)->GetPrimitiveArrayCritical(env, viewboxarray, 0);
	viewbox.x0 = viewboxarr[0];
	viewbox.y0 = viewboxarr[1];
	viewbox.x1 = viewboxarr[2];
	viewbox.y1 = viewboxarr[3];
	(*env)->ReleasePrimitiveArrayCritical(env, viewboxarray, viewboxarr, 0);
	DEBUG("Viewbox: %d %d %d %d",
			viewbox.x0, viewbox.y0, viewbox.x1, viewbox.y1);

	/* do the rendering */
	DEBUG("doing the rendering...");
	buffer = (*env)->GetPrimitiveArrayCritical(env, bufferarray, 0);

	pixmap->x = viewbox.x0;
	pixmap->y = viewbox.y0;
	pixmap->w = viewbox.x1 - viewbox.x0;
	pixmap->h = viewbox.y1 - viewbox.y0;
	pixmap->n = 4;
	pixmap->samples = (void*)buffer;

	// white:
	memset(pixmap->samples, 0xff, pixmap->w * pixmap->h * pixmap->n);

	// do the actual rendering:
	error = fz_rendertreeover(doc->rast, pixmap, page->page->tree, ctm);

	/* evil magic: we transform the rendered image's byte order
	 */
	if(!error) {
		DEBUG("Converting image buffer pixel order");
		length = pixmap->w * pixmap->h;
		unsigned int *col = pixmap->samples;
		int c = 0;
		for(val = 0; val < length; val++) {
			col[val] = ((col[val] & 0xFF000000) >> 24) |
					((col[val] & 0x00FF0000) >> 8) |
					((col[val] & 0x0000FF00) << 8);
		}
	}

	(*env)->ReleasePrimitiveArrayCritical(env, bufferarray, buffer, 0);

	fz_free(pixmap);

	if (error) {
		DEBUG("error!");
		throw_exception(env, EXC_PAGERENDER, "error rendering page");
	}

	DEBUG("PdfView.nativeCreateView() done");
}
