package com.iqonic.woobox.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iqonic.woobox.AppBaseActivity
import com.iqonic.woobox.R
import com.iqonic.woobox.base.BaseRecyclerAdapter
import com.iqonic.woobox.databinding.ItemOrderlistBinding
import com.iqonic.woobox.models.MyOrderData
import com.iqonic.woobox.models.RequestModel
import com.iqonic.woobox.utils.Constants
import com.iqonic.woobox.utils.Constants.KeyIntent.DATA
import com.iqonic.woobox.utils.Constants.KeyIntent.POSITION
import com.iqonic.woobox.utils.Constants.OrderStatus.CANCELLED
import com.iqonic.woobox.utils.Constants.OrderStatus.COMPLETED
import com.iqonic.woobox.utils.Constants.OrderStatus.ONHOLD
import com.iqonic.woobox.utils.Constants.OrderStatus.PENDING
import com.iqonic.woobox.utils.Constants.OrderStatus.PROCESSING
import com.iqonic.woobox.utils.Constants.OrderStatus.REFUNDED
import com.iqonic.woobox.utils.Constants.RequestCode.ORDER_DETAIL
import com.iqonic.woobox.utils.extensions.*
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.layout_nodata.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlin.math.roundToInt

class OrderActivity : AppBaseActivity() {

    private var countLoadMore = 1
    private lateinit var mOrderAdapter: BaseRecyclerAdapter<MyOrderData, ItemOrderlistBinding>

    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        setToolbar(toolbar)
        title = getString(R.string.title_my_orders)

        mOrderAdapter = object : BaseRecyclerAdapter<MyOrderData, ItemOrderlistBinding>() {
            override fun onItemLongClick(view: View, model: MyOrderData, position: Int) {

            }

            override fun onItemClick(view: View, model: MyOrderData, position: Int, dataBinding: ItemOrderlistBinding) {
                if (view.id == R.id.rlMainOrder) {
                    launchActivity<OrderDescriptionActivity>(requestCode = ORDER_DETAIL) {
                        putExtra(DATA, model)
                        putExtra(POSITION, position)
                    }
                } else if (view.id == R.id.btnCancelOrder) {
                    getAlertDialog("Do you want to cancel order?", onPositiveClick = { dialog, i ->
                        dialog.dismiss()

                        cancelMyOrder(model.id, position)
                    }, onNegativeClick = { dialog, i ->
                        dialog.dismiss()
                    }).show()

                }
            }

            override val layoutResId: Int = R.layout.item_orderlist

            override fun onBindData(model: MyOrderData, position: Int, dataBinding: ItemOrderlistBinding) {
                dataBinding.tvOriginalPrice.applyStrike()
                if (model.line_items.size > 0) {
                    dataBinding.tvProductName.text = model.line_items[0].name
                } else {
                    dataBinding.tvProductName.text = "No Products"
                }
                dataBinding.tvPrice.text = (model.total.toFloat() - model.discount_total.toFloat()).roundToInt().toString().currencyFormat(model.currency)
                if (model.discount_total == 0.0) {
                    dataBinding.tvOriginalPrice.hide()
                } else {
                    dataBinding.tvOriginalPrice.show()
                    dataBinding.tvOriginalPrice.text = model.total.toFloat().roundToInt().toString().currencyFormat(model.currency)
                }
                dataBinding.ivCircle.setCircleColor(color(R.color.track_yellow))
                dataBinding.btnCancelOrder.hide()
                when (model.status) {
                    PENDING -> {
                        dataBinding.tvTrack1.text = (toDate(model.date_created) + "<br/>Order <font color=#ECC134>Pending</font>").getHtmlString()
                        dataBinding.tvTrack2.text = getString(R.string.lbl_order_pending).getHtmlString()
                        dataBinding.ivCircle1.setCircleColor(color(R.color.track_yellow))
                        dataBinding.btnCancelOrder.show()
                    }
                    PROCESSING -> {
                        dataBinding.tvTrack1.text = (toDate(model.date_created) + "<br/>Order <font color=#64B931>Processing</font>").getHtmlString()
                        dataBinding.tvTrack2.text = getString(R.string.lbl_item_delivering).getHtmlString()
                        dataBinding.ivCircle.setCircleColor(color(R.color.track_green))
                        dataBinding.ivCircle1.setCircleColor(color(R.color.track_yellow))
                    }
                    ONHOLD -> {
                        dataBinding.tvTrack1.text = (toDate(model.date_created) + "<br/>Order <font color=#ECC134>On Hold</font>").getHtmlString()
                        dataBinding.tvTrack2.text = "Order on hold".getHtmlString()
                        dataBinding.ivCircle1.setCircleColor(color(R.color.track_yellow))
                    }
                    COMPLETED -> {
                        dataBinding.tvTrack1.text = (toDate(model.date_created) + "<br/>Order <font color=#64B931>Placed</font>").getHtmlString()
                        dataBinding.tvTrack2.text = "Order <font color=#64B931>Completed</font>".getHtmlString()
                        dataBinding.tvProductDeliveryDate.text = toDate(model.date_completed!!)
                        dataBinding.ivCircle.setCircleColor(color(R.color.track_green))
                        dataBinding.ivLine.setLineColor(color(R.color.track_green))
                        dataBinding.ivCircle1.setCircleColor(color(R.color.track_green))
                    }
                    CANCELLED -> {
                        dataBinding.ivCircle.setCircleColor(color(R.color.track_red))
                        dataBinding.tvTrack1.text = (toDate(model.date_created) + "<br/>Order <font color=#F61929>Cancelled</font>").getHtmlString()
                        dataBinding.tvTrack2.text = "Order Cancelled".getHtmlString()
                        dataBinding.ivCircle1.setCircleColor(color(R.color.track_red))
                    }
                    REFUNDED -> {
                        dataBinding.ivCircle.setCircleColor(color(R.color.track_grey))
                        dataBinding.tvTrack1.text = (toDate(model.date_created) + "<br/>Order <font color=#D3D3D3>Refunded</font>").getHtmlString()
                        dataBinding.tvTrack2.text = "Order Refunded".getHtmlString()
                        dataBinding.ivCircle1.setCircleColor(color(R.color.track_yellow))
                    }
                    else -> {
                        dataBinding.ivCircle.setCircleColor(color(R.color.track_red))
                        dataBinding.tvTrack1.text = (toDate(model.date_created) + "<br/>Order <font color=#F61929>Trashed</font>").getHtmlString()
                        dataBinding.tvTrack2.text = "Order Trashed"
                        dataBinding.ivCircle1.setCircleColor(color(R.color.track_red))
                    }
                }

                if (model.status == COMPLETED) {
                    dataBinding.llDeliveryDate.show()
                    dataBinding.llDeliveryInfo.show()
                    dataBinding.rlStatus.hide()
                    dataBinding.tvProductDeliveryDate.text = toDate(model.date_completed!!)
                } else {
                    dataBinding.llDeliveryDate.hide()
                    dataBinding.llDeliveryInfo.hide()
                    dataBinding.rlStatus.show()
                }
            }
        }
        rvOrder.adapter = mOrderAdapter
        /* val list = Gson().fromJson<ArrayList<MyOrderData>>(getSharedPrefInstance().getStringValue(Constants.SharedPref.KEY_ORDERS), object : TypeToken<ArrayList<MyOrderData>>() {}.type)
         if (list != null) {
             mOrderAdapter.addItems(list)
         }*/
        loadOrder(countLoadMore)

        val linearLayoutManager = LinearLayoutManager(this)
        rvOrder.layoutManager = linearLayoutManager

        rvOrder.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val countItem = linearLayoutManager.itemCount
                val lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()

                val isLastPosition = countItem.minus(1) == lastVisiblePosition

                if (!isLoading && isLastPosition) {
                    isLoading = true
                    countLoadMore = countLoadMore.plus(1)
                    loadOrder(countLoadMore)
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == ORDER_DETAIL && data != null) {
            val orderModel = data.getSerializableExtra(Constants.KeyIntent.DATA) as MyOrderData
            val position = data.getIntExtra(Constants.KeyIntent.POSITION, -1)
            if (position != -1) {
                mOrderAdapter.mModelList[position] = orderModel
                mOrderAdapter.notifyItemChanged(position)
            }
        }
    }

    private fun cancelMyOrder(orderId: Int, index: Int) {
        progressBar.show()
        cancelOrder(orderId, onApiSuccess = {
            if (it.status == "cancelled") {
                mOrderAdapter.mModelList[index] = it
                mOrderAdapter.notifyItemChanged(index)
            }
            progressBar.hide()
        }, onError = {
            progressBar.hide()

        })
    }

    private fun loadOrder(page: Int) {
        progressBar.show()
        callApi(getRestApis().listAllOrders(getUserId().toInt(), page), onApiSuccess = {
            isLoading = it.size != 10; progressBar.hide()
            mOrderAdapter.addMoreItems(it)
            if (mOrderAdapter.itemCount == 0) rlNoData.show() else rlNoData.hide()
        }, onApiError = {
            progressBar.hide(); snackBarError(it)
        }, onNetworkError = {
            progressBar.hide(); noInternetSnackBar()
        })
    }

}
