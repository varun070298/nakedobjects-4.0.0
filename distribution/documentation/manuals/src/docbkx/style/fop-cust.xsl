<?xml version="1.0"?>

<!-- 

    This is the XSL FO (PDF) stylesheet for the Spring reference
    documentation.
    
    Thanks are due to Christian Bauer of the Hibernate project
    team for writing the original stylesheet upon which this one
    is based.
-->

<!DOCTYPE xsl:stylesheet [
    <!ENTITY admon_gfx_path     "../images/admons/">
    <!ENTITY copyright "&#xA9;">
]>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0"
                xmlns="http://www.w3.org/TR/xhtml1/transitional"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                exclude-result-prefixes="#default">
                
<xsl:import href="urn:docbkx:stylesheet"/>

<!-- 
<xsl:param name="draft.mode">yes</xsl:param>
 -->

<!--###################################################
                      Extensions
    ################################################### -->  

    <!-- These extensions are required for table printing and other stuff -->
    <xsl:param name="use.extensions">1</xsl:param>
    <xsl:param name="tablecolumns.extension">0</xsl:param>
<!--
    <xsl:param name="callout.extensions">1</xsl:param>
-->
    <!-- FOP provide only PDF Bookmarks at the moment -->
    <xsl:param name="fop1.extension">1</xsl:param>
    <xsl:param name="fop1.extensions">1</xsl:param>
<!--
    <xsl:param name="graphicsize.extension">1</xsl:param>
    <xsl:param name="graphicsize.use.img.src.path">0</xsl:param>
	<xsl:param name="ignore.image.scaling" select="0"></xsl:param>
-->

  
<!--###################################################
                   Custom Title Page
    ################################################### --> 
    
    <xsl:template name="book.titlepage.recto">
        <fo:block margin-left="-12mm">
            <fo:table table-layout="fixed" width="175mm">
                <fo:table-column column-width="175mm"/>
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell text-align="center">
                            <fo:block padding-before="45mm">
                            <fo:external-graphic height="5cm" scaling="uniform">
				<xsl:attribute name="src">
					<xsl:text>url(</xsl:text>
					<xsl:value-of select="$img.src.path"/>
					<xsl:text>images/logo.jpg)</xsl:text>
				</xsl:attribute>
	  		    </fo:external-graphic>
								
                            </fo:block>
                            <fo:block font-family="Helvetica" font-size="16pt" padding-before="40mm">
                                <xsl:value-of select="bookinfo/title"/>
                            </fo:block>
                            <fo:block font-family="Helvetica" font-size="14pt">
                                <xsl:value-of select="bookinfo/subtitle"/> 
                            </fo:block>
			    <fo:block font-family="Helvetica" font-size="12pt">
                                <xsl:text>Version </xsl:text><xsl:value-of select="bookinfo/releaseinfo"/>  
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row>
                        <fo:table-cell text-align="center">
                            <fo:block font-family="Helvetica" font-size="12pt">
                                <xsl:value-of select="bookinfo/pubdate"/>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row>
                        <fo:table-cell text-align="center">
                            <fo:block font-family="Helvetica" font-size="10pt" padding-before="80mm">
                                <xsl:text>Copyright &copyright; 2004-2008 </xsl:text>
                                <xsl:for-each select="bookinfo/authorgroup/author">
                                    <xsl:if test="position() > 1">
                                        <xsl:text>, </xsl:text>
                                    </xsl:if>
                                    <xsl:value-of select="firstname"/>
                                    <xsl:text> </xsl:text>
                                    <xsl:value-of select="surname"/>
                                </xsl:for-each>
                            </fo:block>
                            <fo:block font-family="Helvetica" font-size="9pt" padding="1mm">
                                <xsl:value-of select="bookinfo/legalnotice"/>  
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:block>
    </xsl:template>

    <!-- Prevent blank pages in output -->    
    <xsl:template name="book.titlepage.before.verso">
    </xsl:template>
    <xsl:template name="book.titlepage.verso">
    </xsl:template>
    <xsl:template name="book.titlepage.separator">
    </xsl:template>
        
<!--###################################################
                      Header
    ################################################### -->  
	
    <!-- More space in the center header for long text -->
	<xsl:param name="header.column.widths">1 0 1</xsl:param>
	<xsl:param name="header.rule">0</xsl:param>
	
    <xsl:attribute-set name="header.content.properties">
    <xsl:attribute name="font-family">sans-serif</xsl:attribute>
    <xsl:attribute name="font-size">8pt</xsl:attribute>
        
<!--
        <xsl:attribute name="font-family">
            <xsl:value-of select="$body.fontset"/>
        </xsl:attribute>

        <xsl:attribute name="margin-left">-5em</xsl:attribute>
        <xsl:attribute name="margin-right">-5em</xsl:attribute>
-->
    </xsl:attribute-set>


	<xsl:template name="header.content">
	  <xsl:param name="pageclass" select="''"/>
	  <xsl:param name="sequence" select="''"/>
	  <xsl:param name="position" select="''"/>
	  <xsl:param name="gentext-key" select="''"/>
<!--
	  <fo:block>
	    <xsl:value-of select="$pageclass"/>
	    <xsl:text>, </xsl:text>
	    <xsl:value-of select="$sequence"/>
	    <xsl:text>, </xsl:text>
	    <xsl:value-of select="$position"/>
	    <xsl:text>, </xsl:text>
	    <xsl:value-of select="$gentext-key"/>
	  </fo:block>
-->

	  <fo:block>

	    <!-- sequence can be odd, even, first, blank -->
	    <!-- position can be left, center, right -->
	    <xsl:choose>

	      <xsl:when test="$sequence = 'blank' or $sequence = 'first'">
	        <!-- nothing for blank pages -->
	      </xsl:when>

		  <xsl:when test="$pageclass = 'lot' and $position='right'">
	        <xsl:text>Contents</xsl:text>
	      </xsl:when>

 		  <xsl:when test="$position='right'">
	        <xsl:if test="$pageclass != 'titlepage'">
				  <fo:retrieve-marker retrieve-class-name="section.head.marker"
	                                  retrieve-position="first-including-carryover"
	                                  retrieve-boundary="page-sequence"/>
	        </xsl:if>
	      </xsl:when>

  	 	  <xsl:when test="$position='left'">
	        <xsl:if test="$pageclass != 'titlepage'">
               <xsl:apply-templates select="." mode="titleabbrev.markup"/>
	        </xsl:if>
	      </xsl:when>

<!--
  	 	  <xsl:when test="$position='right'">
	        <xsl:if test="$pageclass != 'titlepage'">
               <xsl:apply-templates select="." mode="titleabbrev.markup"/>
	        </xsl:if>
	      </xsl:when>

	      <xsl:when test="($sequence='odd' or $sequence='even') and $position='center'">
	        <xsl:if test="$pageclass != 'titlepage'">

	          <xsl:choose>
	            <xsl:when test="ancestor::book and ($double.sided != 0)">
	              <fo:retrieve-marker retrieve-class-name="section.head.marker"
	                                  retrieve-position="first-including-carryover"
	                                  retrieve-boundary="page-sequence"/>
					<xsl:apply-templates select="." mode="title.markup"/>
	            </xsl:when>
	            <xsl:otherwise>
	              <xsl:apply-templates select="." mode="titleabbrev.markup"/>
	            </xsl:otherwise>
	          </xsl:choose>
	        </xsl:if>
	      </xsl:when>
-->

  	 	  <xsl:when test="$position='center'">
	       	<xsl:text> </xsl:text>
	      </xsl:when>

		  <xsl:when test="$double.sided != 0">
	        <xsl:if test="$pageclass != 'titlepage'">
	          <xsl:choose>
	            	<xsl:when test="$sequence = 'even' and $position='left'">
						<xsl:if test="$pageclass != 'titlepage'">
			               <xsl:apply-templates select="." mode="titleabbrev.markup"/>
				        </xsl:if>
					</xsl:when>
					<xsl:when test="$sequence = 'odd' and $position='right'">
						<xsl:if test="$pageclass != 'titlepage'">
			               <xsl:apply-templates select="." mode="titleabbrev.markup"/>
				        </xsl:if>
					</xsl:when>
					<xsl:when test="$sequence = 'first' and $position='right'">
						<xsl:if test="$pageclass != 'titlepage'">
			               <xsl:apply-templates select="." mode="titleabbrev.markup"/>
				        </xsl:if>
					</xsl:when>
	          </xsl:choose>
	        </xsl:if>
	      </xsl:when>


	    </xsl:choose>
	  </fo:block>
	</xsl:template>

	<xsl:template name="head.sep.rule">
	  <xsl:param name="pageclass"/>
	  <xsl:param name="sequence"/>
	  <xsl:param name="gentext-key"/>

	  <xsl:if test="$header.rule != 0">
		<xsl:choose>
		  <xsl:when test="$pageclass = 'titlepage'">
			<!-- off -->
		  </xsl:when>
		  <xsl:when test="$pageclass = 'body' and $sequence = 'first'">
			<!-- off -->
		  </xsl:when>
		  <xsl:otherwise>
			<xsl:attribute name="border-bottom-width">0pt</xsl:attribute>
			<xsl:attribute name="border-bottom-style">solid</xsl:attribute>
			<xsl:attribute name="border-bottom-color">black</xsl:attribute>
		  </xsl:otherwise>
		</xsl:choose>
	  </xsl:if>
	</xsl:template>
	
<!--###################################################
                      Custom Footer
    ################################################### -->  
	
	<xsl:param name="footer.rule">0</xsl:param>

    <xsl:attribute-set name="footer.content.properties">
        <xsl:attribute name="font-family">sans-serif</xsl:attribute>
        <xsl:attribute name="font-size">8pt</xsl:attribute>
    </xsl:attribute-set>
   
	<xsl:template name="footer.content">
		<xsl:param name="pageclass" select="''" />
		<xsl:param name="sequence" select="''" />
		<xsl:param name="position" select="''" />
		<xsl:param name="gentext-key" select="''" />
		<xsl:variable name="Version">
			<xsl:if test="//releaseinfo">
				<xsl:value-of select="//bookinfo/title" /><xsl:text> (</xsl:text><xsl:value-of select="//releaseinfo" /><xsl:text>)</xsl:text>
			</xsl:if>
		</xsl:variable>

		<xsl:choose>
			<xsl:when test="$sequence='blank'">
<!--
				<xsl:if test="$position = 'center'">
					<xsl:value-of select="$Version" />
				</xsl:if>
-->
			</xsl:when>

			<!-- for double sided printing, print page numbers on alternating sides (of the page) -->
			<xsl:when test="$double.sided != 0">
				<xsl:choose>
					<xsl:when test="$sequence = 'even' and $position='left'">
						<fo:page-number />
					</xsl:when>
					<xsl:when test="$sequence = 'odd' and $position='right'">
						<fo:page-number />
					</xsl:when>
					<xsl:when test="$sequence = 'first' and $position='right'">
						<fo:page-number />
					</xsl:when>
					<xsl:when test="$sequence = 'odd' and $position='left'">
						<xsl:value-of select="$Version" />
					</xsl:when>
				</xsl:choose>
			</xsl:when>

			<!-- for single sided printing, print all page numbers on the right (of the page) -->
			<xsl:when test="$double.sided = 0">
				<xsl:choose>
					<xsl:when test="$position='left'">
						<xsl:value-of select="$Version" />
					</xsl:when>
					<xsl:when test="$position='right'">
						<fo:page-number />
					</xsl:when>
				</xsl:choose>
			</xsl:when>
		</xsl:choose>
	</xsl:template>   
	

    
<!--###################################################
                   Custom Toc Line
    ################################################### -->
    
    <!-- The default DocBook XSL TOC printing is seriously broken... -->
    <xsl:template name="xxxtoc.line">
        <xsl:variable name="id">
            <xsl:call-template name="object.id"/>
        </xsl:variable>

        <xsl:variable name="label">
            <xsl:apply-templates select="." mode="label.markup"/>
        </xsl:variable>

        <!-- justify-end removed from block attributes (space problem in title.markup) -->
        <fo:block  end-indent="{$toc.indent.width}pt"
                   last-line-end-indent="-{$toc.indent.width}pt"
                   white-space-treatment="preserve"
                   text-align="left"
                   white-space-collapse="false">
            <fo:inline keep-with-next.within-line="always">
                <!-- print Chapters in bold style -->
                <xsl:choose>
                    <xsl:when test="local-name(.) = 'chapter'">
                        <xsl:attribute name="font-weight">bold</xsl:attribute>
                    </xsl:when>
                </xsl:choose>        
                <fo:basic-link internal-destination="{$id}">
                    <xsl:if test="$label != ''">
                        <xsl:copy-of select="$label"/>
                        <fo:inline white-space-treatment="preserve"
                                    white-space-collapse="false">
                            <xsl:value-of select="$autotoc.label.separator"/>
                        </fo:inline>
                    </xsl:if>
                    <xsl:apply-templates select="." mode="title.markup"/>
                </fo:basic-link>
            </fo:inline>
            <fo:inline keep-together.within-line="always">
            <xsl:text> </xsl:text>
            <fo:leader leader-pattern="dots"
                        leader-pattern-width="3pt"
                        leader-alignment="reference-area"
                        keep-with-next.within-line="always"/>
            <xsl:text> </xsl:text>
            <fo:basic-link internal-destination="{$id}">
                <fo:page-number-citation ref-id="{$id}"/>
            </fo:basic-link>
            </fo:inline>
        </fo:block>
    </xsl:template>


<!--###################################################
                      Table Of Contents
    ################################################### -->   

    <!-- Generate the TOCs for named components only -->
    <xsl:param name="generate.toc">
        book   toc
    </xsl:param>
    
    <!-- Show only Sections up to level 3 in the TOCs -->
    <xsl:param name="toc.section.depth">1</xsl:param>
    
    <!-- Dot and Whitespace as separator in TOC between Label and Title-->
    <xsl:param name="autotoc.label.separator" select="'.  '"/>

	<xsl:attribute-set name="toc.line.properties">
	  <xsl:attribute name="font-weight">
		<xsl:choose>
		    <xsl:when test="self::chapter | self::preface | self::appendix">bold</xsl:when>
		    <xsl:otherwise>normal</xsl:otherwise>
		</xsl:choose>
	  </xsl:attribute>
	</xsl:attribute-set>

        
<!--###################################################
                   Paper & Page Size
    ################################################### -->  
    
    <!-- Paper type, no headers on blank pages, no double sided printing -->
    <xsl:param name="paper.type" select="'A4'"/>
    <xsl:param name="double.sided">1</xsl:param>
    <xsl:param name="headers.on.blank.pages">0</xsl:param>
    <xsl:param name="footers.on.blank.pages">0</xsl:param>

    <!-- Space between paper border and content (chaotic stuff, don't touch) -->
    <xsl:param name="page.margin.top">5mm</xsl:param>
    <xsl:param name="region.before.extent">10mm</xsl:param>
    <xsl:param name="body.margin.top">10mm</xsl:param>
    
    <xsl:param name="body.margin.bottom">15mm</xsl:param>
    <xsl:param name="region.after.extent">10mm</xsl:param>
    <xsl:param name="page.margin.bottom">5mm</xsl:param>
    
    <xsl:param name="page.margin.outer">18mm</xsl:param>
    <xsl:param name="page.margin.inner">30mm</xsl:param>

    <!-- No intendation of Titles -->
    <xsl:param name="title.margin.left">0pc</xsl:param>

<!--###################################################
                   Fonts & Styles
    ################################################### -->      

    <!-- Left aligned text and no hyphenation -->
    <xsl:param name="alignment">justify</xsl:param>
    <xsl:param name="hyphenate">true</xsl:param>

    <!-- Default Font size -->
	<xsl:param name="body.font.family">serif</xsl:param>
    <xsl:param name="body.font.master">11</xsl:param>
    <xsl:param name="body.font.small">9</xsl:param>

    <!-- Line height in body text -->
    <xsl:param name="line-height">1.4</xsl:param>

    <!-- Monospaced fonts are smaller than regular text -->
    <xsl:attribute-set name="monospace.properties">
        <xsl:attribute name="font-family">
            <xsl:value-of select="$monospace.font.family"/>
        </xsl:attribute>
        <xsl:attribute name="font-size">0.9em</xsl:attribute>
    </xsl:attribute-set>
    
    <xsl:param name="body.start.indent">0em</xsl:param>
<!--
	<xsl:param name="body.start.indent">
	  <xsl:choose>
	    <xsl:when test="$fop.extensions != 0">0pt</xsl:when>
	    <xsl:when test="$passivetex.extensions != 0">0pt</xsl:when>
	    <xsl:otherwise>4pc</xsl:otherwise>
	  </xsl:choose>
	</xsl:param>
-->
<!--###################################################
                   Tables
    ################################################### -->

    <!-- The table width should be adapted to the paper size -->
<!--  	<xsl:param name="default.table.width">17.4cm</xsl:param> -->
<!--
	<xsl:param name="default.table.width">15cm</xsl:param>
-->
    <!-- Some padding inside tables -->    
    <xsl:attribute-set name="table.cell.padding">
        <xsl:attribute name="padding-left">6pt</xsl:attribute>
        <xsl:attribute name="padding-right">6pt</xsl:attribute>
        <xsl:attribute name="padding-top">3pt</xsl:attribute>
        <xsl:attribute name="padding-bottom">2pt</xsl:attribute>
    </xsl:attribute-set>
    
    <!-- Only hairlines as frame and cell borders in tables -->
    <xsl:param name="table.frame.border.thickness">0.2pt</xsl:param>
    <xsl:param name="table.cell.border.thickness">0.2pt</xsl:param>
        
<!--###################################################
                         Labels
    ################################################### -->   

    <!-- Label Chapters and Sections (numbering) -->
    <xsl:param name="chapter.autolabel" select="1"/>
    <xsl:param name="section.autolabel" select="0"/>
    <xsl:param name="section.autolabel.max.depth" select="1"/>
    <xsl:param name="section.label.includes.component.label" select="1"/>

<!--###################################################
                         Titles
    ################################################### -->   
	

	<xsl:template match="part/title" mode="part.titlepage.recto.auto.mode">
		<fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format" font-size="17.8832pt" font-weight="normal" 
			space-before="5cm" space-before.conditionality="retain" >
			Part <xsl:number format="I" level="any" count="part"/>
		</fo:block>
		<fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format" font-size="24.8832pt" font-weight="bold" 
			space-before="0cm" text-align="start" space-after="4cm">
			<xsl:value-of select="."/>
		</fo:block>
	</xsl:template>

	<xsl:template match="chapter/title" mode="chapter.titlepage.recto.auto.mode">
		<fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format" font-size="17.8832pt" font-weight="normal" 
			space-before="5cm" space-before.conditionality="retain" >
			Chapter <xsl:number format="1" level="any" count="chapter"/>
		</fo:block>
		<fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format" font-size="24.8832pt" font-weight="bold" 
			space-before="0cm" text-align="start" space-after="4cm">
			<xsl:value-of select="."/>
		</fo:block>
	</xsl:template>
	
	
	
	<!--
	<xsl:template match="chapter/title" mode="chapter.titlepage.recto.auto.mode">
		<fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format" xsl:use-attribute-sets="chapter.titlepage.recto.style" font-size="24.8832pt" font-weight="bold">
			<xsl:call-template name="chapter.title">
				<xsl:with-param name="node" select="ancestor-or-self::chapter[1]"/>
			</xsl:call-template>
			
		</fo:block>
	</xsl:template>

	<xsl:template name="chapter.title">
	  <xsl:param name="node" select="."/>
	  <xsl:param name="pagewide" select="0"/>

	  <xsl:variable name="id">
		<xsl:call-template name="object.id">
		  <xsl:with-param name="object" select="$node"/>
		</xsl:call-template>
	  </xsl:variable>

	  <xsl:variable name="title">
		<xsl:apply-templates select="$node" mode="object.title.markup">
		  <xsl:with-param name="allow-anchors" select="1"/>
		</xsl:apply-templates>
	  </xsl:variable>

	  <xsl:variable name="titleabbrev">
		<xsl:apply-templates select="$node" mode="titleabbrev.markup"/>
	  </xsl:variable>
	  
	  
	  <xsl:variable name="level">
		<xsl:choose>
		  <xsl:when test="ancestor::section">
			<xsl:value-of select="count(ancestor::section)+1"/>
		  </xsl:when>
		  <xsl:when test="ancestor::sect5">6</xsl:when>
		  <xsl:when test="ancestor::sect4">5</xsl:when>
		  <xsl:when test="ancestor::sect3">4</xsl:when>
		  <xsl:when test="ancestor::sect2">3</xsl:when>
		  <xsl:when test="ancestor::sect1">2</xsl:when>
		  <xsl:otherwise>1</xsl:otherwise>
		</xsl:choose>
	  </xsl:variable>


	  <xsl:if test="$passivetex.extensions != 0">
		<fotex:bookmark xmlns:fotex="http://www.tug.org/fotex"
						fotex-bookmark-level="2"
						fotex-bookmark-label="{$id}">
		  <xsl:value-of select="$titleabbrev"/>
		</fotex:bookmark>
	  </xsl:if>

  
	  <fo:block>
		  <xsl:call-template name="gentext">
			  <xsl:with-param name="key" select="'chapter '"/>
			</xsl:call-template>

			<xsl:apply-templates select="." mode="label.markup"/>
		</fo:block>

	  <fo:block>
		<xsl:apply-templates select="." mode="title.markup"/> 
	  </fo:block>
	  
	</xsl:template>

	-->

    
    <!-- Chapter title size -->
	<!--
    <xsl:attribute-set name="chapter.titlepage.recto.style">
        <xsl:attribute name="text-align">left</xsl:attribute>
        <xsl:attribute name="font-weight">bold</xsl:attribute>
      

		 <xsl:attribute name="font-size">
            <xsl:value-of select="$body.font.master * 1.8"/>
            <xsl:text>pt</xsl:text>
        </xsl:attribute>        
		
        <xsl:attribute name="space-after">1.0cm</xsl:attribute>
        <xsl:attribute name="space-before">5cm</xsl:attribute>
        <xsl:attribute name="space-before.conditionality">retain</xsl:attribute>
        <xsl:attribute name="space-after">4cm</xsl:attribute>

        <xsl:attribute name="border-bottom">3px solid black</xsl:attribute>
        <xsl:attribute name="padding-bottom">5px</xsl:attribute>

	</xsl:attribute-set>
	
	-->
    <!-- Why is the font-size for chapters hardcoded in the XSL FO templates? 
        Let's remove it, so this sucker can use our attribute-set only... -->
		<!--
    <xsl:template match="title" mode="chapter.titlepage.recto.auto.mode">
        <fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format"
                  xsl:use-attribute-sets="chapter.titlepage.recto.style">
            <xsl:call-template name="component.title">
                <xsl:with-param name="node" select="ancestor-or-self::chapter[1]"/>
            </xsl:call-template>
            </fo:block>
    </xsl:template>
    -->
	
	
	
    <!-- Sections 1, 2 and 3 titles have a small bump factor and padding -->    
    <xsl:attribute-set name="section.title.level1.properties">
        <xsl:attribute name="space-before">2.5em</xsl:attribute>
        <xsl:attribute name="font-size">
            <xsl:value-of select="$body.font.master * 1.2"/>
            <xsl:text>pt</xsl:text>
        </xsl:attribute>
        <xsl:attribute name="space-after">0.1em</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="section.title.level2.properties">
        <xsl:attribute name="space-before">1.9em</xsl:attribute>
        <xsl:attribute name="font-size">
            <xsl:value-of select="$body.font.master * 1.1"/>
            <xsl:text>pt</xsl:text>
        </xsl:attribute>
        <xsl:attribute name="space-after">0.1em</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="section.title.level3.properties">
        <xsl:attribute name="space-before.optimum">0.4em</xsl:attribute>
        <xsl:attribute name="space-before.minimum">0.4em</xsl:attribute>
        <xsl:attribute name="space-before.maximum">0.4em</xsl:attribute>
        <xsl:attribute name="font-size">
            <xsl:value-of select="$body.font.master * 1.0"/>
            <xsl:text>pt</xsl:text>
        </xsl:attribute>
        <xsl:attribute name="space-after.optimum">0.1em</xsl:attribute>
        <xsl:attribute name="space-after.minimum">0.1em</xsl:attribute>
        <xsl:attribute name="space-after.maximum">0.1em</xsl:attribute>
    </xsl:attribute-set>

<!--
    <xsl:attribute-set name="section.title.level6.properties">
        <xsl:attribute name="space-before.optimum">0.4em</xsl:attribute>
        <xsl:attribute name="space-before.minimum">0.4em</xsl:attribute>
        <xsl:attribute name="space-before.maximum">0.4em</xsl:attribute>
        <xsl:attribute name="font-size">
            <xsl:value-of select="$body.font.master * 0.5"/>
            <xsl:text>pt</xsl:text>
        </xsl:attribute>
        <xsl:attribute name="space-after.optimum">0.1em</xsl:attribute>
        <xsl:attribute name="space-after.minimum">0.1em</xsl:attribute>
        <xsl:attribute name="space-after.maximum">0.1em</xsl:attribute>
    </xsl:attribute-set>
	
	
	<xsl:attribute-set name="section.title.level2.properties">
   <xsl:attribute name="text-align">left</xsl:attribute>
   <xsl:attribute name="start-indent">3em</xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="section.title.level3.properties">
   <xsl:attribute name="text-align">left</xsl:attribute>
   <xsl:attribute name="start-indent">5em</xsl:attribute>
</xsl:attribute-set>
	
-->

    <!-- Titles of formal objects (tables, examples, ...) -->
    <xsl:attribute-set name="formal.title.properties" use-attribute-sets="normal.para.spacing">
        <xsl:attribute name="font-weight">bold</xsl:attribute>
        <xsl:attribute name="font-size">
            <xsl:value-of select="$body.font.master"/>
            <xsl:text>pt</xsl:text>
        </xsl:attribute>
        <xsl:attribute name="hyphenate">false</xsl:attribute>
        <xsl:attribute name="space-after.minimum">0.4em</xsl:attribute>
        <xsl:attribute name="space-after.optimum">0.6em</xsl:attribute>
        <xsl:attribute name="space-after.maximum">0.8em</xsl:attribute>
    </xsl:attribute-set>    

<!--###################################################
                      Formal paras
    ################################################### -->  
  <xsl:template match="formalpara/title">
     <fo:block xsl:use-attribute-sets="section.title.properties">
        <xsl:apply-templates/>
     </fo:block>
  </xsl:template>
<!--###################################################
                      Programlistings
    ################################################### -->  
    
    <!-- Verbatim text formatting (programlistings) -->
<!--	<xsl:param name="hyphenate.verbatim" select="1"></xsl:param> -->

    <xsl:attribute-set name="monospace.verbatim.properties">
        <xsl:attribute name="font-size">
            <xsl:value-of select="$body.font.small * 0.9"/>
            <xsl:text>pt</xsl:text>
        </xsl:attribute>
		<xsl:attribute name="wrap-option">wrap</xsl:attribute>
		<!--
		<xsl:attribute name="hyphenation-character">!</xsl:attribute>
		-->
    </xsl:attribute-set>
    
    <xsl:attribute-set name="verbatim.properties">
        <xsl:attribute name="space-before.minimum">1em</xsl:attribute>
        <xsl:attribute name="space-before.optimum">1em</xsl:attribute>
        <xsl:attribute name="space-before.maximum">1em</xsl:attribute>
        <!-- 
        <xsl:attribute name="border-color">#444444</xsl:attribute>
        <xsl:attribute name="border-style">solid</xsl:attribute>
        <xsl:attribute name="border-width">0.2pt</xsl:attribute>      
        <xsl:attribute name="padding-top">0.5em</xsl:attribute>      
        <xsl:attribute name="padding-left">0.5em</xsl:attribute>      
        <xsl:attribute name="padding-right">0.5em</xsl:attribute>      
        <xsl:attribute name="padding-bottom">0.5em</xsl:attribute>      
        <xsl:attribute name="margin-left">0.5em</xsl:attribute>      
        <xsl:attribute name="margin-right">0.5em</xsl:attribute>      
         -->
    </xsl:attribute-set>    

    <!-- Shade (background) programlistings -->    
<!--
    <xsl:param name="shade.verbatim">1</xsl:param>
    <xsl:attribute-set name="shade.verbatim.style">
        <xsl:attribute name="background-color">#f0f0f0</xsl:attribute>
    </xsl:attribute-set>
    -->
    
    <xsl:template match="programlistings/emphasis"> 
        <fo:inline font-weight="bold">
            <xsl:apply-templates/>
        </fo:inline> 
    </xsl:template> 
    
                
<!--###################################################
                         Callouts
    ################################################### -->   

    <!-- Use images for callouts instead of (1) (2) (3) -->
    <xsl:param name="callout.graphics">0</xsl:param>
    <xsl:param name="callout.unicode">1</xsl:param>
    
    <!-- Place callout marks at this column in annotated areas -->
    <xsl:param name="callout.defaultcolumn">90</xsl:param>

<!--###################################################
                       Admonitions
    ################################################### -->   

    <!-- Use nice graphics for admonitions -->
    <xsl:param name="admon.graphics">'1'</xsl:param>
    <xsl:param name="admon.graphics.path">&admon_gfx_path;</xsl:param>

<!--###################################################
                          Misc
    ################################################### -->   

    <!-- Placement of titles -->
    <xsl:param name="formal.title.placement">
        figure after
        example before
        equation before
        table before
        procedure before
    </xsl:param>
    
    <!-- Format Variable Lists as Blocks (prevents horizontal overflow) -->
    <xsl:param name="variablelist.as.blocks">0</xsl:param>
	<xsl:param name="variablelist.term.break.after">0</xsl:param>
	<xsl:param name="variablelist.term.separator">, </xsl:param>
    
    <!-- The horrible list spacing problems -->
    <xsl:attribute-set name="list.block.spacing">
        <xsl:attribute name="space-before.optimum">0.1em</xsl:attribute>
        <xsl:attribute name="space-before.minimum">0.1em</xsl:attribute>
        <xsl:attribute name="space-before.maximum">0.1em</xsl:attribute>
        <xsl:attribute name="space-after.optimum">0.1em</xsl:attribute>
        <xsl:attribute name="space-after.minimum">0.1em</xsl:attribute>
        <xsl:attribute name="space-after.maximum">0.1em</xsl:attribute>
    </xsl:attribute-set>

	<xsl:attribute-set name="list.item.spacing">
	  <xsl:attribute name="space-before.optimum">0.3em</xsl:attribute>
	  <xsl:attribute name="space-before.minimum">0.2em</xsl:attribute>
	  <xsl:attribute name="space-before.maximum">0.8em</xsl:attribute>
	</xsl:attribute-set>
    
<!--###################################################
              colored and hyphenated links 
    ################################################### --> 
	<xsl:template match="ulink"> 
	<fo:basic-link external-destination="{@url}" 
			xsl:use-attribute-sets="xref.properties" 
			text-decoration="underline" 
			color="blue"> 
			<xsl:choose> 
			<xsl:when test="count(child::node())=0"> 
			<xsl:value-of select="@url"/> 
			</xsl:when> 
			<xsl:otherwise> 
			<xsl:apply-templates/> 
			</xsl:otherwise> 
			</xsl:choose> 
			</fo:basic-link> 
	</xsl:template> 
	
    <xsl:template match="varlistentry/term"> 
    <fo:inline font-style="italic"> 
            <xsl:apply-templates/>
    </fo:inline> 
    </xsl:template> 
<!-- 
    <xsl:template match="variablelist/title"> 
    <fo:inline font-style="italic"> 
            <xsl:apply-templates/>
    </fo:inline> 
    </xsl:template> 
 -->
    
<!--
<xsl:template match="title" mode="list.title.mode">
	<fo:block font-size="10pt" font-weight="bold" xsl:use-attribute-sets="normal.para.spacing">
		<xsl:apply-templates/>
	</fo:block>
</xsl:template>
-->

   <xsl:template match="markup"> 
        <fo:inline font-weight="bold">
            <xsl:apply-templates/>
        </fo:inline> 
    </xsl:template> 

	
	
	 <xsl:template match="screenshot"> 
        <fo:block space-before="20px">
            <xsl:apply-templates/>
        </fo:block> 
    </xsl:template> 

	
	 <xsl:template match="remark"> 
        <fo:block padding="10px" space-before="20px" color="red" border-style="solid" border-width="1px" border-color="red">
            <xsl:apply-templates/>
        </fo:block> 
    </xsl:template> 
    

	
</xsl:stylesheet>
