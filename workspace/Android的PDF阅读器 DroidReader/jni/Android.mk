# Copyright (C) 2010 Hans-Werner Hilse <hilse@web.de>
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.




LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

# jbig2dec
# uses pristine source tree

LOCAL_MODULE := jbig2dec

# Homepage: http://jbig2dec.sourceforge.net/
# Original License: GPL v.3, see jbig2dec/COPYING
# Original Copyright (C) 2001-2009 Artifex Software, Inc.

LOCAL_SRC_FILES := \
	jbig2dec/jbig2.c \
	jbig2dec/jbig2_arith.c \
	jbig2dec/jbig2_arith_int.c \
	jbig2dec/jbig2_arith_iaid.c \
	jbig2dec/jbig2_huffman.c \
	jbig2dec/jbig2_segment.c \
	jbig2dec/jbig2_page.c \
	jbig2dec/jbig2_symbol_dict.c \
	jbig2dec/jbig2_text.c \
	jbig2dec/jbig2_generic.c \
	jbig2dec/jbig2_refinement.c \
	jbig2dec/jbig2_mmr.c \
	jbig2dec/jbig2_halftone.c \
	jbig2dec/jbig2_image.c \
	jbig2dec/jbig2_image_pbm.c \
	jbig2dec/jbig2_metadata.c

LOCAL_C_INCLUDES := \
	$(LOCAL_PATH)/jbig2dec-overlay
LOCAL_CFLAGS := -DHAVE_CONFIG_H

include $(BUILD_STATIC_LIBRARY)
include $(CLEAR_VARS)


# openjpeg
# uses pristine source tree

LOCAL_MODULE := openjpeg

# Homepage: http://www.openjpeg.org/
# Original License: see openjpeg/license.txt
# Original Copyrights:
# * Copyright (c) 2002-2007, Communications and Remote Sensing Laboratory, Universite catholique de Louvain (UCL), Belgium
# * Copyright (c) 2002-2007, Professor Benoit Macq
# * Copyright (c) 2001-2003, David Janssens
# * Copyright (c) 2002-2003, Yannick Verschueren
# * Copyright (c) 2003-2007, Francois-Olivier Devaux and Antonin Descampe
# * Copyright (c) 2005, Herve Drolon, FreeImage Team

LOCAL_SRC_FILES := \
	openjpeg/libopenjpeg/bio.c \
	openjpeg/libopenjpeg/cio.c \
	openjpeg/libopenjpeg/dwt.c \
	openjpeg/libopenjpeg/event.c \
	openjpeg/libopenjpeg/image.c \
	openjpeg/libopenjpeg/j2k.c \
	openjpeg/libopenjpeg/j2k_lib.c \
	openjpeg/libopenjpeg/jp2.c \
	openjpeg/libopenjpeg/jpt.c \
	openjpeg/libopenjpeg/mct.c \
	openjpeg/libopenjpeg/mqc.c \
	openjpeg/libopenjpeg/openjpeg.c \
	openjpeg/libopenjpeg/pi.c \
	openjpeg/libopenjpeg/raw.c \
	openjpeg/libopenjpeg/t1.c \
	openjpeg/libopenjpeg/t2.c \
	openjpeg/libopenjpeg/tcd.c \
	openjpeg/libopenjpeg/tgt.c

include $(BUILD_STATIC_LIBRARY)
include $(CLEAR_VARS)


# jpeg
# uses pristine source tree

LOCAL_MODULE := jpeg

# Homepage: http://www.ijg.org/
# Original License: see jpeg/README
# Original Copyright (C) 1991-2009, Thomas G. Lane, Guido Vollbeding

LOCAL_SRC_FILES := \
	jpeg/jcapimin.c \
	jpeg/jcapistd.c \
	jpeg/jcarith.c \
	jpeg/jctrans.c \
	jpeg/jcparam.c \
	jpeg/jdatadst.c \
	jpeg/jcinit.c \
	jpeg/jcmaster.c \
	jpeg/jcmarker.c \
	jpeg/jcmainct.c \
	jpeg/jcprepct.c \
	jpeg/jccoefct.c \
	jpeg/jccolor.c \
	jpeg/jcsample.c \
	jpeg/jchuff.c \
	jpeg/jcdctmgr.c \
	jpeg/jfdctfst.c \
	jpeg/jfdctflt.c \
	jpeg/jfdctint.c \
	jpeg/jdapimin.c \
	jpeg/jdapistd.c \
	jpeg/jdarith.c \
	jpeg/jdtrans.c \
	jpeg/jdatasrc.c \
	jpeg/jdmaster.c \
	jpeg/jdinput.c \
	jpeg/jdmarker.c \
	jpeg/jdhuff.c \
	jpeg/jdmainct.c \
	jpeg/jdcoefct.c \
	jpeg/jdpostct.c \
	jpeg/jddctmgr.c \
	jpeg/jidctfst.c \
	jpeg/jidctflt.c \
	jpeg/jidctint.c \
	jpeg/jdsample.c \
	jpeg/jdcolor.c \
	jpeg/jquant1.c \
	jpeg/jquant2.c \
	jpeg/jdmerge.c \
	jpeg/jaricom.c \
	jpeg/jcomapi.c \
	jpeg/jutils.c \
	jpeg/jerror.c \
	jpeg/jmemmgr.c \
	jpeg/jmemnobs.c

include $(BUILD_STATIC_LIBRARY)
include $(CLEAR_VARS)


# freetype
# (flat file hierarchy, use 
# "cp .../freetype-.../src/*/*.[ch] freetype/"
#  and copy over the full include/ subdirectory)

LOCAL_MODULE := freetype

# Homepage: http://freetype.org/
# Original License: GPL 2 (or its own, but for the purposes
#                   of this project, GPL is fine)
# 

LOCAL_C_INCLUDES := \
	$(LOCAL_PATH)/freetype-overlay/include \
	$(LOCAL_PATH)/freetype/include

LOCAL_CFLAGS := -DFT2_BUILD_LIBRARY

# libz provided by the Android-3 Stable Native API:
LOCAL_LDLIBS := -lz

# see freetype/doc/INSTALL.ANY for further customization,
# currently, all sources are being built
LOCAL_SRC_FILES := \
	freetype/src/base/ftsystem.c \
	freetype/src/base/ftinit.c \
	freetype/src/base/ftdebug.c \
	freetype/src/base/ftbase.c \
	freetype/src/base/ftbbox.c \
	freetype/src/base/ftglyph.c \
	freetype/src/base/ftbitmap.c \
	freetype/src/base/ftcid.c \
	freetype/src/base/ftfstype.c \
	freetype/src/base/ftgasp.c \
	freetype/src/base/ftgxval.c \
	freetype/src/base/ftlcdfil.c \
	freetype/src/base/ftmm.c \
	freetype/src/base/ftotval.c \
	freetype/src/base/ftpatent.c \
	freetype/src/base/ftstroke.c \
	freetype/src/base/ftsynth.c \
	freetype/src/base/fttype1.c \
	freetype/src/base/ftxf86.c \
	freetype/src/cff/cff.c \
	freetype/src/cid/type1cid.c \
	freetype/src/sfnt/sfnt.c \
	freetype/src/truetype/truetype.c \
	freetype/src/type1/type1.c \
	freetype/src/raster/raster.c \
	freetype/src/smooth/smooth.c \
	freetype/src/autofit/autofit.c \
	freetype/src/cache/ftcache.c \
	freetype/src/gxvalid/gxvalid.c \
	freetype/src/otvalid/otvalid.c \
	freetype/src/psaux/psaux.c \
	freetype/src/pshinter/pshinter.c \
	freetype/src/psnames/psnames.c

include $(BUILD_STATIC_LIBRARY)
include $(CLEAR_VARS)


# mupdf
# pristine source tree

LOCAL_MODULE := mupdf

# Homepage: http://ccxvii.net/mupdf/
# License: GPL 3
# MuPDF is Copyright 2006-2009 Artifex Software, Inc. 

LOCAL_C_INCLUDES := \
	$(LOCAL_PATH)/freetype/include \
	$(LOCAL_PATH)/jpeg \
	$(LOCAL_PATH)/jbig2dec \
	$(LOCAL_PATH)/openjpeg/libopenjpeg \
	$(LOCAL_PATH)/mupdf/fitzdraw \
	$(LOCAL_PATH)/mupdf/fitz \
	$(LOCAL_PATH)/mupdf/mupdf \
	$(LOCAL_PATH)

LOCAL_CFLAGS := -Drestrict= -DEXTERNALFONTS

# support for OpenJPEG / JBIG2dec:
LOCAL_CFLAGS += -DHAVE_OPENJPEG=yes
LOCAL_CFLAGS += -DHAVE_JBIG2DEC=yes

# debugging:
#LOCAL_CFLAGS += -DMUPDF_DEBUG=yes

LOCAL_SRC_FILES := \
	mupdf/mupdf/pdf_crypt.c \
	mupdf-overlay/mupdf/pdf_debug.c \
	mupdf/mupdf/pdf_lex.c \
	mupdf/mupdf/pdf_nametree.c \
	mupdf/mupdf/pdf_open.c \
	mupdf/mupdf/pdf_parse.c \
	mupdf/mupdf/pdf_repair.c \
	mupdf/mupdf/pdf_stream.c \
	mupdf/mupdf/pdf_xref.c \
	mupdf/mupdf/pdf_annot.c \
	mupdf/mupdf/pdf_outline.c \
	mupdf/mupdf/pdf_cmap.c \
	mupdf/mupdf/pdf_cmap_parse.c \
	mupdf-overlay/mupdf/pdf_cmap_load.c \
	mupdf/mupdf/pdf_fontagl.c \
	mupdf/mupdf/pdf_fontenc.c \
	mupdf/mupdf/pdf_unicode.c \
	mupdf/mupdf/pdf_font.c \
	mupdf/mupdf/pdf_type3.c \
	mupdf/mupdf/pdf_fontmtx.c \
	mupdf-overlay/mupdf/pdf_fontfile.c \
	mupdf/mupdf/pdf_function.c \
	mupdf/mupdf/pdf_colorspace1.c \
	mupdf/mupdf/pdf_colorspace2.c \
	mupdf/mupdf/pdf_image.c \
	mupdf/mupdf/pdf_pattern.c \
	mupdf/mupdf/pdf_shade.c \
	mupdf/mupdf/pdf_shade1.c \
	mupdf/mupdf/pdf_shade4.c \
	mupdf/mupdf/pdf_xobject.c \
	mupdf/mupdf/pdf_build.c \
	mupdf/mupdf/pdf_interpret.c \
	mupdf/mupdf/pdf_page.c \
	mupdf/mupdf/pdf_pagetree.c \
	mupdf/mupdf/pdf_store.c \
	mupdf/fitzdraw/glyphcache.c \
	mupdf/fitzdraw/porterduff.c \
	mupdf/fitzdraw/meshdraw.c \
	mupdf/fitzdraw/imagedraw.c \
	mupdf/fitzdraw/imageunpack.c \
	mupdf/fitzdraw/imagescale.c \
	mupdf/fitzdraw/pathscan.c \
	mupdf/fitzdraw/pathfill.c \
	mupdf/fitzdraw/pathstroke.c \
	mupdf/fitzdraw/pixmap.c \
	mupdf/fitzdraw/render.c \
	mupdf/fitzdraw/blendmodes.c \
	mupdf/fitz/base_cpudep.c \
	mupdf-overlay/fitz/base_error.c \
	mupdf/fitz/base_hash.c \
	mupdf/fitz/base_matrix.c \
	mupdf/fitz/base_memory.c \
	mupdf/fitz/base_rect.c \
	mupdf/fitz/base_string.c \
	mupdf/fitz/base_unicode.c \
	mupdf/fitz/util_getopt.c \
	mupdf/fitz/crypt_aes.c \
	mupdf/fitz/crypt_arc4.c \
	mupdf/fitz/crypt_crc32.c \
	mupdf/fitz/crypt_md5.c \
	mupdf/fitz/obj_array.c \
	mupdf/fitz/obj_dict.c \
	mupdf/fitz/obj_parse.c \
	mupdf/fitz/obj_print.c \
	mupdf/fitz/obj_simple.c \
	mupdf/fitz/stm_buffer.c \
	mupdf/fitz/stm_filter.c \
	mupdf/fitz/stm_open.c \
	mupdf/fitz/stm_read.c \
	mupdf/fitz/stm_misc.c \
	mupdf/fitz/filt_pipeline.c \
	mupdf/fitz/filt_basic.c \
	mupdf/fitz/filt_arc4.c \
	mupdf/fitz/filt_aesd.c \
	mupdf/fitz/filt_dctd.c \
	mupdf/fitz/filt_faxd.c \
	mupdf/fitz/filt_faxdtab.c \
	mupdf/fitz/filt_flate.c \
	mupdf/fitz/filt_lzwd.c \
	mupdf/fitz/filt_predict.c \
	mupdf/fitz/node_toxml.c \
	mupdf/fitz/node_misc1.c \
	mupdf/fitz/node_misc2.c \
	mupdf/fitz/node_path.c \
	mupdf/fitz/node_text.c \
	mupdf/fitz/node_tree.c \
	mupdf/fitz/res_colorspace.c \
	mupdf/fitz/res_font.c \
	mupdf/fitz/res_image.c \
	mupdf/fitz/res_shade.c

# support for OpenJPEG / JBIG2dec:
LOCAL_SRC_FILES += \
	mupdf/fitz/filt_jbig2d.c \
	mupdf/fitz/filt_jpxd.c

# uses libz, which is officially supported for NDK API
LOCAL_LDLIBS := -lz -llog
LOCAL_STATIC_LIBRARIES := freetype jpeg openjpeg jbig2dec

include $(BUILD_STATIC_LIBRARY)
include $(CLEAR_VARS)

# and finally, the module for our JNI interface, which is compiled
# to a shared library which then includes only the needed parts
# from the static archive we compiled above

LOCAL_MODULE    := pdfrender
# debugging:
#LOCAL_CFLAGS	:= -DPDFRENDER_DEBUG=yes
LOCAL_SRC_FILES := \
	pdfrender.c

LOCAL_C_INCLUDES := \
	$(LOCAL_PATH)/mupdf/fitz \
	$(LOCAL_PATH)/mupdf/mupdf

LOCAL_STATIC_LIBRARIES := mupdf freetype jpeg openjpeg jbig2dec

# uses Android log and z library (Android-3 Native API)
LOCAL_LDLIBS := -llog -lz

include $(BUILD_SHARED_LIBRARY)

