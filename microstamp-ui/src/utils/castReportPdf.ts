import jsPDF from 'jspdf';

const PAGE_MARGIN = 48;

const COLORS = {
    textDark: '#1F2430',
    textMuted: '#6B7280',
    accent: '#96651E',
    accentBg: '#FBF1E4',
    border: '#E1DED8'
};



export interface PdfRef {
    label: string;
    target?: string;
}

class PdfBuilder {
    doc: jsPDF;
    pageWidth: number;
    pageHeight: number;
    contentWidth: number;
    cursorY: number;
    private destinations = new Map<string, { pageNumber: number; top: number }>();
    // Link targets may appear later in the document, so each link keeps a mutable
    // options object that is resolved in finalize(), before the PDF is serialized.
    private pendingLinks: { target: string; options: Record<string, unknown> }[] = [];

    constructor() {
        this.doc = new jsPDF({ unit: 'pt', format: 'a4' });
        this.pageWidth = this.doc.internal.pageSize.getWidth();
        this.pageHeight = this.doc.internal.pageSize.getHeight();
        this.contentWidth = this.pageWidth - PAGE_MARGIN * 2;
        this.cursorY = PAGE_MARGIN;
    }

    private ensureSpace(height: number) {
        if (this.cursorY + height > this.pageHeight - PAGE_MARGIN) {
            this.doc.addPage();
            this.cursorY = PAGE_MARGIN;
        }
    }

    currentPage() {
        return this.doc.getCurrentPageInfo().pageNumber;
    }

    private registerAnchor(anchor: string) {
        this.destinations.set(anchor, { pageNumber: this.currentPage(), top: Math.max(this.cursorY - 16, 0) });
    }

    private internalLink(x: number, y: number, w: number, h: number, target: string) {
        const options: Record<string, unknown> = {};
        this.pendingLinks.push({ target, options });
        this.doc.link(x, y, w, h, options);
    }

    bookmark(title: string, parent: unknown = null) {
        return this.doc.outline.add(parent, title, { pageNumber: this.currentPage() });
    }

    mainTitle(title: string, metaLines: string[]) {
        this.doc.setFont('helvetica', 'bold');
        this.doc.setFontSize(22);
        this.doc.setTextColor(COLORS.textDark);
        this.doc.text(title, PAGE_MARGIN, this.cursorY + 20);
        this.cursorY += 32;

        this.doc.setFont('helvetica', 'normal');
        this.doc.setFontSize(9.5);
        this.doc.setTextColor(COLORS.textMuted);
        metaLines.forEach(line => {
            this.doc.text(line, PAGE_MARGIN, this.cursorY);
            this.cursorY += 13;
        });

        this.cursorY += 8;
        this.doc.setDrawColor(COLORS.accent);
        this.doc.setLineWidth(1.6);
        this.doc.line(PAGE_MARGIN, this.cursorY, this.pageWidth - PAGE_MARGIN, this.cursorY);
        this.cursorY += 28;
    }

    sectionTitle(title: string) {
        // Reserve room for the heading plus at least one content line, so a
        // section title never ends up alone at the bottom of a page.
        this.ensureSpace(46);
        this.doc.setFont('helvetica', 'bold');
        this.doc.setFontSize(13.5);
        this.doc.setTextColor(COLORS.accent);
        this.doc.text(title.toUpperCase(), PAGE_MARGIN, this.cursorY);
        this.cursorY += 8;
        this.doc.setDrawColor(COLORS.accent);
        this.doc.setLineWidth(1);
        this.doc.line(PAGE_MARGIN, this.cursorY, this.pageWidth - PAGE_MARGIN, this.cursorY);
        this.cursorY += 20;
    }

    paragraph(text: string, opts: { italic?: boolean } = {}) {
        this.doc.setFont('helvetica', opts.italic ? 'italic' : 'normal');
        this.doc.setFontSize(10);
        this.doc.setTextColor(COLORS.textDark);
        const lines = this.doc.splitTextToSize(text, this.contentWidth);
        const lineHeight = 13.5;
        this.ensureSpace(lines.length * lineHeight);
        this.doc.text(lines, PAGE_MARGIN, this.cursorY + 8);
        this.cursorY += lines.length * lineHeight + 4;
    }

    labeledField(label: string, value: string, target?: string) {
        this.doc.setFont('helvetica', 'normal');
        this.doc.setFontSize(9.8);
        const valueLines = this.doc.splitTextToSize(value, this.contentWidth) as string[];
        this.ensureSpace(11 + valueLines.length * 13 + 6);

        this.fieldLabel(label);

        this.doc.setFont('helvetica', 'normal');
        this.doc.setFontSize(9.8);
        this.doc.setTextColor(target ? COLORS.accent : COLORS.textDark);
        this.doc.text(valueLines, PAGE_MARGIN, this.cursorY);
        if (target) {
            const width = Math.max(...valueLines.map(line => this.doc.getTextWidth(line)));
            this.internalLink(PAGE_MARGIN, this.cursorY - 10, width, valueLines.length * 13, target);
        }
        this.cursorY += valueLines.length * 13 + 6;
    }

    private fieldLabel(label: string) {
        this.doc.setFont('helvetica', 'bold');
        this.doc.setFontSize(8);
        this.doc.setTextColor(COLORS.textMuted);
        this.doc.text(label.toUpperCase(), PAGE_MARGIN, this.cursorY);
        this.cursorY += 11;
    }

    itemHeading(code: string | undefined, title: string | undefined, anchor?: string) {
        this.ensureSpace(20);
        if (anchor) this.registerAnchor(anchor);
        this.doc.setFont('helvetica', 'bold');
        this.doc.setFontSize(10.5);

        let x = PAGE_MARGIN;
        if (code) {
            this.doc.setTextColor(COLORS.accent);
            this.doc.text(code, x, this.cursorY);
            x += this.doc.getTextWidth(code) + 8;
        }
        if (title) {
            this.doc.setTextColor(COLORS.textDark);
            const availableWidth = this.pageWidth - PAGE_MARGIN - x;
            const titleLines = this.doc.splitTextToSize(title, availableWidth);
            this.doc.text(titleLines[0], x, this.cursorY);
            if (titleLines.length > 1) {
                this.cursorY += 14;
                this.ensureSpace((titleLines.length - 1) * 14);
                this.doc.text(titleLines.slice(1), PAGE_MARGIN, this.cursorY);
                this.cursorY += (titleLines.length - 1) * 14;
            }
        }
        this.cursorY += 15;
    }

    chips(label: string, items: PdfRef[]) {
        if (items.length === 0) return;

        this.ensureSpace(11 + 24);
        this.fieldLabel(label);

        // Cross-references can carry a full free-text description (e.g. a Systemic
        // Factor's category + description), so each chip wraps its own text onto
        // multiple lines within a capped width instead of overflowing past the
        // page edge or the row of chips next to it.
        const maxChipWidth = Math.min(this.contentWidth, 260);
        const paddingX = 8;
        const paddingY = 5;
        const lineHeight = 10.5;
        const gapX = 8;
        const gapY = 6;

        this.doc.setFont('helvetica', 'normal');
        this.doc.setFontSize(8.3);

        let x = PAGE_MARGIN;
        let rowTop = this.cursorY;
        let rowHeight = 0;

        items.forEach(({ label: text, target }) => {
            this.doc.setFont('helvetica', 'normal');
            this.doc.setFontSize(8.3);
            const lines = this.doc.splitTextToSize(text, maxChipWidth - paddingX * 2) as string[];
            const textWidth = Math.max(...lines.map(line => this.doc.getTextWidth(line)));
            const chipWidth = textWidth + paddingX * 2;
            const chipHeight = lines.length * lineHeight + paddingY * 2;

            if (x > PAGE_MARGIN && x + chipWidth > this.pageWidth - PAGE_MARGIN) {
                x = PAGE_MARGIN;
                this.cursorY = rowTop + rowHeight + gapY;
                rowTop = this.cursorY;
                rowHeight = 0;
            }
            if (x === PAGE_MARGIN) {
                this.ensureSpace(chipHeight);
                rowTop = this.cursorY;
            }

            // Linked chips use the accent style; a reference whose item no longer exists is greyed out.
            this.doc.setDrawColor(target ? COLORS.accent : COLORS.border);
            this.doc.setFillColor(target ? COLORS.accentBg : '#F5F5F4');
            this.doc.roundedRect(x, rowTop, chipWidth, chipHeight, 3, 3, 'FD');

            this.doc.setTextColor(target ? COLORS.accent : COLORS.textMuted);
            lines.forEach((line, i) => {
                this.doc.text(line, x + paddingX, rowTop + paddingY + lineHeight * (i + 0.75));
            });
            if (target) this.internalLink(x, rowTop, chipWidth, chipHeight, target);

            x += chipWidth + gapX;
            rowHeight = Math.max(rowHeight, chipHeight);
        });

        this.cursorY = rowTop + rowHeight + 6;
    }

    divider() {
        this.doc.setDrawColor(COLORS.border);
        this.doc.setLineWidth(0.6);
        this.doc.line(PAGE_MARGIN, this.cursorY, this.pageWidth - PAGE_MARGIN, this.cursorY);
        this.cursorY += 14;
    }

    emptyState(label: string, editUrl: string) {
        this.ensureSpace(30);
        this.doc.setFont('helvetica', 'italic');
        this.doc.setFontSize(9.5);
        this.doc.setTextColor(COLORS.textMuted);
        this.doc.text(label, PAGE_MARGIN, this.cursorY);
        this.cursorY += 16;

        this.doc.setFont('helvetica', 'normal');
        this.doc.setFontSize(9.5);
        this.doc.setTextColor(COLORS.accent);
        this.doc.textWithLink('Edit this section »', PAGE_MARGIN, this.cursorY, { url: editUrl });
        this.cursorY += 22;
    }

    spacer(height: number) {
        this.cursorY += height;
    }

    finalize(fileName: string) {
        this.pendingLinks.forEach(({ target, options }) => {
            const destination = this.destinations.get(target);
            if (destination) Object.assign(options, destination);
        });

        const totalPages = this.doc.getNumberOfPages();
        for (let i = 1; i <= totalPages; i++) {
            this.doc.setPage(i);
            this.doc.setFont('helvetica', 'normal');
            this.doc.setFontSize(8);
            this.doc.setTextColor(COLORS.textMuted);
            this.doc.text('CAST Accident Analysis Report', PAGE_MARGIN, this.pageHeight - 24);
            this.doc.text(`Page ${i} of ${totalPages}`, this.pageWidth - PAGE_MARGIN, this.pageHeight - 24, {
                align: 'right'
            });
        }
        this.doc.save(fileName);
    }
}

export interface SectionItem {
    // Entity id; other items link to this one through it.
    anchor?: string;
    // Text shown in the PDF bookmarks panel; defaults to the code.
    bookmark?: string;
    code?: string;
    title?: string;
    paragraphs?: string[];
    fields?: { label: string; value: string; target?: string }[];
    links?: { label: string; refs: PdfRef[] }[];
    referencedBy?: PdfRef[];
}

function writeSection(
    pdf: PdfBuilder,
    title: string,
    items: SectionItem[],
    emptyLabel: string,
    editUrl: string
) {
    pdf.sectionTitle(title);
    const sectionBookmark = pdf.bookmark(title);

    if (items.length === 0) {
        pdf.emptyState(emptyLabel, editUrl);
        return;
    }

    items.forEach((item, index) => {
        pdf.itemHeading(item.code, item.title, item.anchor);
        const bookmark = item.bookmark ?? item.code;
        if (bookmark) pdf.bookmark(bookmark, sectionBookmark);
        item.paragraphs?.forEach(text => pdf.paragraph(text));
        item.fields?.forEach(field => pdf.labeledField(field.label, field.value, field.target));
        item.links?.forEach(group => pdf.chips(group.label, group.refs));
        if (item.referencedBy) pdf.chips('Referenced by', item.referencedBy);

        if (index < items.length - 1) {
            pdf.divider();
        } else {
            pdf.spacer(20);
        }
    });
}

export interface CastReportPdfData {
    analysisId: string;
    executiveSummary: string;
    sections: {
        title: string;
        editUrl: string;
        emptyLabel: string;
        items: SectionItem[];
    }[];
}

export function generateCastReportPdf(data: CastReportPdfData) {
    const pdf = new PdfBuilder();

    pdf.mainTitle('CAST Accident Analysis Report', [
        `Analysis ID: ${data.analysisId}`,
        `Generated on ${new Date().toLocaleString()}`
    ]);

    pdf.sectionTitle('Executive Summary');
    pdf.bookmark('Executive Summary');
    if (data.executiveSummary.trim()) {
        pdf.paragraph(data.executiveSummary);
    } else {
        pdf.paragraph('No executive summary provided.', { italic: true });
    }
    pdf.spacer(10);

    data.sections.forEach(section => {
        writeSection(pdf, section.title, section.items, section.emptyLabel, section.editUrl);
    });

    pdf.finalize(`cast-report-${data.analysisId}.pdf`);
}
