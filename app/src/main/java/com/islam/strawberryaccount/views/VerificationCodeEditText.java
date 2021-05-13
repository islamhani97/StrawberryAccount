package com.islam.strawberryaccount.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.islam.strawberryaccount.R;

/**
 * Verification code EditText.
 *
 * @author ISLAM
 */
public class VerificationCodeEditText extends AppCompatEditText {

    private int textColor;
    private int hintColor;
    private int maxLength;
    private int digitWidth;
    private int digitHeight;
    private int digitPadding;
    private int digitStyle;
    private int strokeWidth;
    private int strokeColor;
    private int digitCompleteStrokeColor;
    private String hintSymbol;
    private Drawable digitDrawable;

    private OnCodeCompleteListener onCodeCompleteListener;
    private Rect textRect;
    private Rect drawableRect;
    private RectF digitRect;
    private Paint digitPaint;


    public VerificationCodeEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VerificationCodeEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        setLayoutDirection(LAYOUT_DIRECTION_LTR);
        setInputType(InputType.TYPE_CLASS_NUMBER);
        textRect = new Rect();
        drawableRect = new Rect();
        digitRect = new RectF();
        digitPaint = new Paint();

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.VerificationCodeEditText);
        try {
            textColor = typedArray.getInteger(R.styleable.VerificationCodeEditText_textColor, Color.BLACK);
            hintColor = typedArray.getInteger(R.styleable.VerificationCodeEditText_hintColor, Color.GRAY);
            maxLength = typedArray.getInteger(R.styleable.VerificationCodeEditText_maxLength, 6);
            digitWidth = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeEditText_digitWidth, 60);
            digitHeight = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeEditText_digitHeight, 60);
            digitPadding = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeEditText_digitPadding, 20);

            hintSymbol = typedArray.getString(R.styleable.VerificationCodeEditText_hintSymbol);
            if (hintSymbol == null) {
                hintSymbol = "_";
            }

            digitDrawable = typedArray.getDrawable(R.styleable.VerificationCodeEditText_digitDrawable);
            strokeWidth = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeEditText_strokeWidth, 2);
            strokeColor = typedArray.getInteger(R.styleable.VerificationCodeEditText_strokeColor, Color.BLACK);
            digitCompleteStrokeColor = typedArray.getInteger(R.styleable.VerificationCodeEditText_digitCompleteStrokeColor, strokeColor);

            TypedValue outValue = new TypedValue();
            boolean isValueExist = typedArray.getValue(R.styleable.VerificationCodeEditText_digitStyle, outValue);
            if (isValueExist) {
                digitStyle = outValue.data;
            } else {
                digitStyle = 1;
            }


        } finally {
            typedArray.recycle();
        }

        //Disable copy paste
        super.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });

        setMaxLength(maxLength);
        setLongClickable(false);
        //Remove background color
        setBackgroundColor(Color.TRANSPARENT);
        //Don't show cursor
        setCursorVisible(false);

//        if (mStrokeDrawable == null) {
//            throw new NullPointerException("stroke drawable not allowed to be null!");
//        }
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        return false;
    }

    void setMaxLength(int maxLength) {
        if (maxLength >= 0) {
            setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        } else {
            setFilters(new InputFilter[0]);
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //Width and height information of the current input box
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //Judge whether the height is less than the recommended height
        if (height < digitHeight) {
            height = digitHeight;
        }

        //Input box width = border width * quantity + border spacing * (quantity-1)
        int recommendWidth = digitWidth * maxLength + digitPadding * (maxLength - 1);
        //Judge whether the width is less than the recommended width
        if (width < recommendWidth) {
            width = recommendWidth;
        }

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, widthMode);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, heightMode);
        //Set up survey layout
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setTextColor(Color.TRANSPARENT);
        super.onDraw(canvas);
        setTextColor(textColor);

        //Redraw background color
        drawDigit(canvas);
        //Redraw text
        drawText(canvas);
    }


    private void drawDigit(Canvas canvas) {

        switch (digitStyle) {
            case 0: {
                drawDrawableDigit(canvas);
                break;
            }
            case 1: {
                drawRectDigit(canvas);
                break;
            }
            case 2: {
                drawLineDigit(canvas);
                break;
            }
        }
    }

    private void drawDrawableDigit(Canvas canvas) {

        drawableRect.left = 0;
        drawableRect.top = 0;
        drawableRect.right = digitWidth;
        drawableRect.bottom = digitHeight;

        int count = canvas.save();

        digitDrawable.setBounds(drawableRect); // set the location
        digitDrawable.setState(new int[]{android.R.attr.state_enabled}); // set image state

        for (int i = 0; i < maxLength; i++) {
            digitDrawable.draw(canvas);
            float dx = digitWidth + digitPadding;
            canvas.translate(dx, 0);
        }
        canvas.restoreToCount(count);

    }

    private void drawRectDigit(Canvas canvas) {

        digitRect.left = strokeWidth / 2f;
        digitRect.top = strokeWidth / 2f;
        digitRect.right = digitWidth - strokeWidth / 2f;
        digitRect.bottom = digitHeight - strokeWidth / 2f;

        digitPaint.setStyle(Paint.Style.STROKE);
        digitPaint.setStrokeWidth(strokeWidth);
        digitPaint.setAntiAlias(true);

        int count = canvas.save();

        int activatedIndex = Math.max(0, getEditableText().length());

        for (int i = 0; i < maxLength; i++) {

            if (i < activatedIndex) {
                digitPaint.setColor(digitCompleteStrokeColor);
            } else {
                digitPaint.setColor(strokeColor);
            }
            canvas.drawRoundRect(digitRect, 10, 10, digitPaint);
            float dx = digitWidth + digitPadding;
            canvas.translate(dx, 0);
        }
        canvas.restoreToCount(count);
    }

    private void drawLineDigit(Canvas canvas) {


        digitPaint.setStrokeWidth(strokeWidth);

        int count = canvas.save();
        int activatedIndex = Math.max(0, getEditableText().length());

        for (int i = 0; i < maxLength; i++) {
            if (i < activatedIndex) {
                digitPaint.setColor(digitCompleteStrokeColor);
            } else {
                digitPaint.setColor(strokeColor);
            }
            canvas.drawLine((digitWidth + digitPadding) * i,
                    digitHeight - strokeWidth / 2f,
                    ((digitWidth + digitPadding) * i) + digitWidth,
                    digitHeight - strokeWidth / 2f,
                    digitPaint
            );


//            float dx = digitWidth + digitPadding;
//            canvas.translate(dx, 0);
        }

        canvas.restoreToCount(count);


    }


    private void drawText(Canvas canvas) {
        textRect.left = 0;
        textRect.top = 0;
        textRect.right = digitWidth;
        textRect.bottom = digitHeight;

        int count = canvas.getSaveCount();

        int length = getEditableText().length();
        for (int i = 0; i < length; i++) {
            String text = String.valueOf(getEditableText().charAt(i));
            TextPaint textPaint = getPaint();
            textPaint.setColor(textColor);
            //Get text size
            textPaint.getTextBounds(text, 0, 1, textRect);
            //Calculate (x, y) coordinates

            int x = digitWidth / 2 + (digitWidth + digitPadding) * i - (textRect.centerX());
            int y = digitHeight / 2 - textRect.centerY();

            canvas.drawText(text, x, y, textPaint);
        }

        if (length < maxLength) {

            for (int i = length; i < maxLength; i++) {

                TextPaint textPaint = getPaint();
                textPaint.setColor(hintColor);
                //Get text size
                textPaint.getTextBounds(hintSymbol, 0, 1, textRect);
                //Calculate (x, y) coordinates
                int x = digitWidth / 2 + (digitWidth + digitPadding) * i - (textRect.centerX());
                int y = digitHeight / 2 - textRect.centerY();
                canvas.drawText(hintSymbol, x, y, textPaint);
            }
        }

        canvas.restoreToCount(count);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        //Current text length
        int textLength = getEditableText().length();

        if (textLength == maxLength) {
//            hideSoftInput();
            if (onCodeCompleteListener != null) {
                onCodeCompleteListener.onCodeComplete(getEditableText().toString());
            }
        }

    }

    public void setOnCodeCompleteListener(OnCodeCompleteListener onCodeCompleteListener) {
        this.onCodeCompleteListener = onCodeCompleteListener;
    }

    public interface OnCodeCompleteListener {
        void onCodeComplete(String code);
    }

}