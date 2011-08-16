package com.gueei.demos.markupDemo.custom_actionbar.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.gueei.demos.markupDemo.R;
import com.gueei.demos.markupDemo.custom_actionbar.collections.ObservableListObservable;
import com.gueei.demos.markupDemo.custom_actionbar.iConst;
import com.gueei.demos.markupDemo.custom_actionbar.observables.NotificationItemDataModel;
import gueei.binding.*;

public class ActionBar extends RelativeLayout implements View.OnClickListener, IBindableView<ActionBar> {

	private Context                                             mContext;
	private TextView                                            mTitleView;
	private LinearLayout                                        mActionsView;
	private LinearLayout                                        mIndicatorsView;
	private ProgressBar                                         mProgress;
	private LinearLayout                                        mActionBar;
	private ObservableListObservable<NotificationItemDataModel> mNotificationList;
	private QuickAction                                         mQuickAction;
	private Command mQABtnClicked;
	private Command                                             mQATopClicked;
	public static final int INDEX_IS_NOT_SET = -1;

	public ActionBar(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		mContext = context;
		RelativeLayout barView = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.custom_ab_widget_actionbar, null);
		addView(barView);
		//--
		mActionBar = (LinearLayout) barView.findViewById(R.id.actionbar);
		setOnActionBarClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				if (null != mQuickAction) {
					mQuickAction.show(v);
				}
			}
		});
		mTitleView = (TextView) barView.findViewById(R.id.actionbar_title);
		mActionsView = (LinearLayout) barView.findViewById(R.id.actionbar_actions);
		mIndicatorsView = (LinearLayout) barView.findViewById(R.id.custom_ab_actionbar_indicators);
		mProgress = (ProgressBar) barView.findViewById(R.id.actionbar_progress);
	}

	@SuppressWarnings({"UnusedDeclaration"})
	public void setTitle(CharSequence title) {
		mTitleView.setText(title);
	}

	public void setProgressBarVisibility(int visibility) {
		mProgress.setVisibility(visibility);
	}

	public void setOnActionBarClickListener(OnClickListener listener) {
		mActionBar.setOnClickListener(listener);
	}

	@Override
	public void onClick(View view) {
		final Object tag = view.getTag();
		if (tag instanceof IAction) {
			final IAction action = (IAction) tag;
			action.performAction(view);
		}
	}

	@SuppressWarnings({"UnusedDeclaration"})
	public void addAction(IAction action) {
		final int index = mActionsView.getChildCount();
		addAction(action, index);
	}

	public void addAction(IAction action, int index) {
		mActionsView.addView(inflateAction(action), index);
	}

	protected int getIndicatorIndex(int drawable) {
		int count = mIndicatorsView.getChildCount();
		int drawableId;
		if (count != 0) {
			for (int index = 0; index < count; ++index) {
				drawableId = (Integer) mIndicatorsView.getChildAt(index).getTag();
				if (drawableId == drawable) {
					return index;
				}
			}
		}
		return INDEX_IS_NOT_SET;
	}

	public void addIndicator(int drawable) {
		if (INDEX_IS_NOT_SET == getIndicatorIndex(drawable)) {
			ImageView imageView = new ImageView(mContext);
			imageView.setImageResource(drawable);
			imageView.setTag(drawable);
			mIndicatorsView.addView(imageView);
		}
	}

	public void removeIndicator(int drawable) {
		int index = getIndicatorIndex(drawable);
		if (index > INDEX_IS_NOT_SET) {
			mIndicatorsView.removeViewAt(index);
		}
	}

	private View inflateAction(IAction action) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.custom_ab_widget_actionbar_item, mActionsView, false);
		ImageButton labelView = (ImageButton) view.findViewById(R.id.actionbar_item);
		labelView.setImageResource(action.getDrawable());
		view.setTag(action);
		view.setOnClickListener(this);
		return view;
	}

	public interface IAction {

		public int getDrawable();

		public void performAction(View view);
	}

	@SuppressWarnings({"UnusedDeclaration"})
	public class QuickIntentAction implements IAction {

		private final int mDrawable;

		public QuickIntentAction(int drawable) {
			mDrawable = drawable;
		}

		@Override public int getDrawable() {
			return mDrawable;
		}

		@Override
		public void performAction(View view) {
			if (null != mQABtnClicked) {
				mQABtnClicked.InvokeCommand(view);
			}
		}
	}

	// AB related
	// View Attribute Caption
	private final ViewAttribute<ActionBar, CharSequence> CaptionViewAttribute              =
			new ViewAttribute<ActionBar, CharSequence>(CharSequence.class, ActionBar.this, iConst.ATTR_CAPTION) {
				@Override protected void doSetAttributeValue(Object newValue) {
					CharSequence newCaption = ((newValue == null) || (newValue.toString().length() < 1)) ? null : newValue.toString();
					mTitleView.setText(newCaption);
				}

				@Override public CharSequence get() {
					return mTitleView.getText();
				}
			};
	// View Attribute Progress Bar State
	private final ViewAttribute<ActionBar, Integer>                  ProgressStateAttribute            =
			new ViewAttribute<ActionBar, Integer>(Integer.class, ActionBar.this, iConst.ATTR_PROGRESS_STATE) {
				@Override protected void doSetAttributeValue(Object newValue) {
					int progressState = ((newValue != null)) ? (Integer) newValue : GONE;
					switch (progressState) {
					case VISIBLE:
					case INVISIBLE:
					case GONE:
						break;
					default:
						progressState = GONE;
					}
					setProgressBarVisibility(progressState);
				}

				@Override public Integer get() {
					return mProgress.getVisibility();
				}
			};
	// View Attribute  ActionBar Visibility State
	private final ViewAttribute<ActionBar, Integer>                  ActionBarVisibilityStateAttribute =
			new ViewAttribute<ActionBar, Integer>(Integer.class, ActionBar.this, iConst.ATTR_VISIBILITY) {
				@Override protected void doSetAttributeValue(Object newValue) {
					int visualState = ((newValue != null)) ? (Integer) newValue : GONE;
					switch (visualState) {
					case VISIBLE:
					case INVISIBLE:
					case GONE:
						break;
					default:
						visualState = GONE;
					}
					setVisibility(visualState);
				}

				@Override public Integer get() {
					return getVisibility();
				}
			};
	// View Attribute Notification Source List
	private final ViewAttribute<ActionBar, ObservableListObservable> NotificationSourceListAttribute   =
			new ViewAttribute<ActionBar, ObservableListObservable>(ObservableListObservable.class, ActionBar.this, iConst.ATTR_NOTIFICATION_SOURCE) {
				@Override
				protected void doSetAttributeValue(Object newValue) {
					if (newValue != null && newValue instanceof ObservableListObservable) {
						//noinspection unchecked
						mNotificationList = (ObservableListObservable<NotificationItemDataModel>) newValue;
						mIndicatorObserver = new CollectionObserver() {
							@Override public void onCollectionChanged(IObservableCollection<?> collection) {
								updateIndicators();
							}
						};
						updateIndicators();
						mNotificationList.subscribe(mIndicatorObserver);
					}
				}

				@Override
				public ObservableListObservable get() {
					return mNotificationList;
				}

				@Override protected BindingType AcceptThisTypeAs(Class<?> type) {
					return BindingType.OneWay;
				}
			};
	// View Attribute  ActionBar Top Quick Action Widget
	private final ViewAttribute<ActionBar, QuickAction>              TopQuickActionAttribute           =
			new ViewAttribute<ActionBar, QuickAction>(QuickAction.class, ActionBar.this, iConst.ATTR_TOP_QUICK_ACTION) {
				@Override protected void doSetAttributeValue(Object newValue) {
					if (newValue != null && newValue instanceof QuickAction) {
						mQuickAction = (QuickAction) newValue;
						mQuickAction.setOnActionListItemClickListener(mActionListItemClickListener);
					}
				}

				@Override public QuickAction get() {
					return mQuickAction;
				}
			};
	// View Attribute  ActionBar Quick Action Button Icon Resource Id
	private final ViewAttribute<ActionBar, Integer>                  QuickActionButtonIconAttribute    =
			new ViewAttribute<ActionBar, Integer>(Integer.class, ActionBar.this, iConst.ATTR_TOP_QUICK_ACTION) {
				@Override protected void doSetAttributeValue(Object newValue) {
					if (newValue != null && newValue instanceof Integer) {
						IAction action = new ActionBar.QuickIntentAction((Integer) newValue);
						addAction(action);
					}
				}

				@Override public Integer get() {
					return null;
				}

				@Override
				protected BindingType AcceptThisTypeAs(Class<?> type) {
					return BindingType.OneWay;
				}
			};
	// View Attribute  ActionBar OnQActionButtonClicked Command Attribute
	private final ViewAttribute<ActionBar, Command>                  OnQActionButtonClickedAttribute   =
			new ViewAttribute<ActionBar, Command>(Command.class, ActionBar.this, iConst.ATTR_ON_Q_ACTION_BUTTON_CLICKED) {
				@Override protected void doSetAttributeValue(Object newValue) {
					if (newValue instanceof Command) {
						mQABtnClicked = (Command) newValue;
					}
				}

				@Override public Command get() {
					return null;
				}

				@Override
				protected BindingType AcceptThisTypeAs(Class<?> type) {
					return BindingType.OneWay;
				}
			};
	// View Attribute  ActionBar OnQTopActionClicked Command Attribute
	private final ViewAttribute<ActionBar, Command>                  OnQTopActionClickedAttribute      =
			new ViewAttribute<ActionBar, Command>(Command.class, ActionBar.this, iConst.ATTR_ON_TOP_ACTION_CLICKED) {
				@Override protected void doSetAttributeValue(Object newValue) {
					if (newValue instanceof Command) {
						mQATopClicked = (Command) newValue;
					}
				}

				@Override public Command get() {
					return null;
				}

				@Override
				protected BindingType AcceptThisTypeAs(Class<?> type) {
					return BindingType.OneWay;
				}
			};
	// View Attribute  ActionBar TopActionClickedCommandFirerIndex Attribute
	private final ViewAttribute<ActionBar, Integer>                  TopActionClickedCommandFirerIndex =
			new ViewAttribute<ActionBar, Integer>(Integer.class, ActionBar.this, iConst.ATTR_TOP_ACTION_CLICKED_COMMAND_FIRER_INDEX) {
				private int mTopQAItemCmdFirerIndex = -1;

				@Override protected void doSetAttributeValue(Object newValue) {
					if (newValue != null && newValue instanceof Integer) {
						mTopQAItemCmdFirerIndex = (Integer) newValue;
					}
				}

				@Override public Integer get() {
					return mTopQAItemCmdFirerIndex;
				}

				@Override
				protected BindingType AcceptThisTypeAs(Class<?> type) {
					return BindingType.TwoWay;
				}
			};

	@Override public ViewAttribute<ActionBar, ?> getViewAttribute(String attributeId) {
		if (iConst.ATTR_CAPTION.equals(attributeId)) {
			return CaptionViewAttribute;
		}
		else if (iConst.ATTR_VISIBILITY.equals(attributeId)) {
			return ActionBarVisibilityStateAttribute;
		}
		else if (iConst.ATTR_PROGRESS_STATE.equals(attributeId)) {
			return ProgressStateAttribute;
		}
		else if (iConst.ATTR_NOTIFICATION_SOURCE.equals(attributeId)) {
			return NotificationSourceListAttribute;
		}
		else if (iConst.ATTR_TOP_QUICK_ACTION.equals(attributeId)) {
			return TopQuickActionAttribute;
		}
		else if (iConst.ATTR_ACTION_BUTTON_ICON_ID.equals(attributeId)) {
			return QuickActionButtonIconAttribute;
		}
		else if (iConst.ATTR_ON_Q_ACTION_BUTTON_CLICKED.equals(attributeId)) {
			return OnQActionButtonClickedAttribute;
		}
		else if (iConst.ATTR_ON_TOP_ACTION_CLICKED.equals(attributeId)) {
			return OnQTopActionClickedAttribute;
		}
		else if (iConst.ATTR_TOP_ACTION_CLICKED_COMMAND_FIRER_INDEX.equals(attributeId)) {
			return TopActionClickedCommandFirerIndex;
		}
		return null;
	}

	// support stuff
	private CollectionObserver mIndicatorObserver;

	public void updateIndicators() {
		for (NotificationItemDataModel item : mNotificationList) {
			if (item.isChecked()) {
				addIndicator(item.getDrawableId());
			}
			else {
				removeIndicator(item.getDrawableId());
			}
		}
	}

	private final QuickAction.OnActionListItemClickListener mActionListItemClickListener = new QuickAction.OnActionListItemClickListener() {
		public void onListItemClick(NotificationItemDataModel item) {
			TopActionClickedCommandFirerIndex.set(mNotificationList.indexOf(item));
			if (null != mQATopClicked) {
				mQATopClicked.InvokeCommand(null);
			}
		}
	};
}
