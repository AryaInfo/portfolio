package com.arya.portfolio.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;


/**
 * The type Justified text view.
 */
public class JustifiedTextView extends View {

    private Context mContext;

    private TextPaint textPaint;

    private int lineSpace = 0;

    private int lineHeight;

    private int textAreaWidth;

    /**
     * The Measured view height.
     */
    public int measuredViewHeight,
    /**
     * The Measured view width.
     */
    measuredViewWidth;

    private String text;

    private List<String> lineList = new ArrayList<>();


    /**
     * The Has text been drown.
     */
    boolean hasTextBeenDrown = false;

    /**
     * Instantiates a new Justified text view.
     *
     * @param context  the context
     * @param attrs    the attrs
     * @param defStyle the def style
     */
    public JustifiedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        constructor(context, attrs);
    }

    /**
     * Instantiates a new Justified text view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public JustifiedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        constructor(context, attrs);
    }

    /**
     * Instantiates a new Justified text view.
     *
     * @param context the context
     */
    public JustifiedTextView(Context context) {
        super(context);
        constructor(context, null);

    }

    private void constructor(Context context, AttributeSet attrs) {

        mContext = context;
        XmlToClassAttribHandler mXmlParser = new XmlToClassAttribHandler(mContext, attrs);
        initTextPaint();

        if (attrs != null) {
            String text;
            int textColor;
            int textSize;
            int textSizeUnit;

            text = mXmlParser.getTextValue();
            textColor = mXmlParser.getColorValue();
            textSize = mXmlParser.getTextSize();
            textSizeUnit = mXmlParser.gettextSizeUnit();


            setText(text);
            setTextColor(textColor);
            if (textSizeUnit == -1)
                setTextSize(textSize);
            else
                setTextSize(textSizeUnit, textSize);

//			setText(XmlToClassAttribHandler.GetAttributeStringValue(mContext, attrs, namespace, key, ""));

        }

        ViewTreeObserver observer = getViewTreeObserver();


        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                if (hasTextBeenDrown)
                    return;
                hasTextBeenDrown = true;
                setTextAreaWidth(getWidth() - (getPaddingLeft() + getPaddingRight()));
                calculate();

            }


        });

    }

    private void calculate() {
        setLineHeight(getTextPaint());
        lineList.clear();
        lineList = divideOriginalTextToStringLineList(getText());
        setMeasuredDimentions(lineList.size(), getLineHeight(), getLineSpace());
        measure(getMeasuredViewWidth(), getMeasuredViewHeight());
    }

    private void initTextPaint() {
        textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.RIGHT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getMeasuredViewWidth() > 0) {
            requestLayout();
            setMeasuredDimension(getMeasuredViewWidth(), getMeasuredViewHeight());
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {

        int rowIndex = getPaddingTop();
        int colIndex;
        if (getAlignment() == Paint.Align.RIGHT)
            colIndex = getPaddingLeft() + getTextAreaWidth();
        else
            colIndex = getPaddingLeft();

        for (int i = 0; i < lineList.size(); i++) {
            rowIndex += getLineHeight() + getLineSpace();

            canvas.drawText(lineList.get(i), colIndex, rowIndex, getTextPaint());
        }

    }


    private List<String> divideOriginalTextToStringLineList(String originalText) {

        List<String> listStringLine = new ArrayList<>();

        String line = "";
        float textWidth;

        String[] listParageraphes = originalText.split("\n");

        for (String listParageraphe : listParageraphes) {
            String[] arrayWords = listParageraphe.split(" ");

            for (int i = 0; i < arrayWords.length; i++) {

                line += arrayWords[i] + " ";
                textWidth = getTextPaint().measureText(line);

                //if text width is equal to textAreaWidth then just add it to ListStringLine
                if (getTextAreaWidth() == textWidth) {

                    listStringLine.add(line);
                    line = "";//make line clear
                    continue;
                }
                //else if text width excite textAreaWidth then remove last word and justify the StringLine
                else if (getTextAreaWidth() < textWidth) {

                    int lastWordCount = arrayWords[i].length();

                    //remove last word that cause line width to excite textAreaWidth
                    line = line.substring(0, line.length() - lastWordCount - 1);

                    // if line is empty then should be skipped
                    if (line.trim().length() == 0)
                        continue;

                    //and then we need to justify line
                    line = justifyTextLine(textPaint, line.trim(), getTextAreaWidth());

                    listStringLine.add(line);
                    line = "";
                    i--;
                    continue;
                }

                //if we are now at last line of paragraph then just add it
                if (i == arrayWords.length - 1) {
                    listStringLine.add(line);
                    line = "";
                }
            }
        }

        return listStringLine;

    }


    private String justifyTextLine(TextPaint textPaint, String lineString, int textAreaWidth) {

        int gapIndex = 0;

        float lineWidth = textPaint.measureText(lineString);

        while (lineWidth < textAreaWidth && lineWidth > 0) {

            gapIndex = lineString.indexOf(" ", gapIndex + 2);
            if (gapIndex == -1) {
                gapIndex = 0;
                gapIndex = lineString.indexOf(" ", gapIndex + 1);
                if (gapIndex == -1)
                    return lineString;
            }

            lineString = lineString.substring(0, gapIndex) + "  " + lineString.substring(gapIndex + 1, lineString.length());

            lineWidth = textPaint.measureText(lineString);
        }
        return lineString;
    }

    private void setLineHeight(TextPaint textPaint) {

        Rect bounds = new Rect();
        String sampleStr = "این حسین کیست که عالم همه دیوانه اوست";
        textPaint.getTextBounds(sampleStr, 0, sampleStr.length(), bounds);

        setLineHeight(bounds.height());

    }

    /**
     * Sets measured dimentions.
     *
     * @param lineListSize the line list size
     * @param lineHeigth   the line heigth
     * @param lineSpace    the line space
     */
    public void setMeasuredDimentions(int lineListSize, int lineHeigth, int lineSpace) {
        int mHeight = lineListSize * (lineHeigth + lineSpace) + lineSpace;

        mHeight += getPaddingRight() + getPaddingLeft();

        setMeasuredViewHeight(mHeight);

        setMeasuredViewWidth(getWidth());
    }


    private int getTextAreaWidth() {
        return textAreaWidth;
    }

    private void setTextAreaWidth(int textAreaWidth) {
        this.textAreaWidth = textAreaWidth;
    }

    private int getLineHeight() {
        return lineHeight;
    }

    private int getMeasuredViewHeight() {
        return measuredViewHeight;
    }

    private void setMeasuredViewHeight(int measuredViewHeight) {
        this.measuredViewHeight = measuredViewHeight;
    }

    private int getMeasuredViewWidth() {
        return measuredViewWidth;
    }

    private void setMeasuredViewWidth(int measuredViewWidth) {
        this.measuredViewWidth = measuredViewWidth;
    }

    private void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
    }

    /**
     * Gets text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets text.
     *
     * @param text the text
     */
    public void setText(String text) {
        this.text = text;
        calculate();
        invalidate();
    }

    /**
     * Sets type face.
     *
     * @param typeFace the type face
     */
    public void setTypeFace(Typeface typeFace) {
        getTextPaint().setTypeface(typeFace);
    }

    /**
     * Gets text size.
     *
     * @param unit     the unit
     * @param textSize the text size
     */
    public void setTextSize(int unit, float textSize) {
        textSize = TypedValue.applyDimension(unit, textSize, mContext.getResources().getDisplayMetrics());
        setTextSize(textSize);
    }

    /**
     * Sets text size.
     *
     * @param textSize the text size
     */
    public void setTextSize(float textSize) {
        getTextPaint().setTextSize(textSize);
        calculate();
        invalidate();
    }

    /**
     * Gets text paint.
     *
     * @return the text paint
     */
    public TextPaint getTextPaint() {
        return textPaint;
    }

    /**
     * Sets text color.
     *
     * @param textColor the text color
     */
    public void setTextColor(int textColor) {
        getTextPaint().setColor(textColor);
        invalidate();
    }

    /**
     * Sets line spacing.
     *
     * @param lineSpace the line space
     */
    public void setLineSpacing(int lineSpace) {
        this.lineSpace = lineSpace;
        invalidate();
    }

    /**
     * Gets text color.
     *
     * @return the text color
     */
    public int getLineSpace() {
        return lineSpace;
    }


    /**
     * Gets alignment.
     *
     * @return the alignment
     */
    public Paint.Align getAlignment() {
        return getTextPaint().getTextAlign();
    }

    /**
     * Sets alignment.
     *
     * @param align the align
     */
    public void setAlignment(Paint.Align align) {
        getTextPaint().setTextAlign(align);
        invalidate();
    }


}
