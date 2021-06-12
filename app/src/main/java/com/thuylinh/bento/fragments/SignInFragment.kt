package com.thuylinh.bento.fragments

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.thuylinh.bento.AppBaseActivity
import com.thuylinh.bento.R
import com.thuylinh.bento.activity.DashBoardActivity
import com.thuylinh.bento.activity.SignInUpActivity
import com.thuylinh.bento.models.RequestModel
import com.thuylinh.bento.utils.extensions.*
import kotlinx.android.synthetic.main.dialog_forgot_password.*
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.layout_social_buttons.*

class SignInFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edtEmail.onFocusChangeListener = this
        edtPassword.onFocusChangeListener = this
        edtPassword.transformationMethod = biggerDotTranformation

        edtEmail.setSelection(edtEmail.length())
        btnSignIn.onClick { if (validate()) doLogin() }
        tvForget.onClick { snackBar(context.getString(R.string.lbl_coming_soon)) }
        ivFaceBook.onClick {
            (activity as SignInUpActivity).doFacebookLogin()
        }
        ivGoogle.onClick {
            (activity as SignInUpActivity).doGoogleLogin()
        }
        btnSignUp.onClick { (activity as SignInUpActivity).loadSignUpFragment() }
        tvForget.onClick { showForgotPasswordDialog() }
    }

    private fun validate(): Boolean {
        return when {
            edtEmail.checkIsEmpty() -> {
                edtEmail.showError(getString(R.string.error_field_required))
                false
            }
            edtPassword.checkIsEmpty() -> {
                edtPassword.showError(getString(R.string.error_field_required))
                false
            }
            else -> true
        }
    }

    private fun doLogin() {
        (activity as AppBaseActivity).signIn(
                edtEmail.textToString(),
                edtPassword.textToString(),
                onResult = {
                    if (it) activity?.launchActivityWithNewTask<DashBoardActivity>()
                },
                onError = {
                    activity?.snackBarError(it)
                })
    }

    private fun showForgotPasswordDialog() {
        val forgotPasswordDialog = Dialog(activity!!)
        forgotPasswordDialog.window?.setBackgroundDrawable(ColorDrawable(0))
        forgotPasswordDialog.setContentView(R.layout.dialog_forgot_password)
        forgotPasswordDialog.window?.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)

        forgotPasswordDialog.apply {
            this.btnForgotPassword.onClick {
                forgotPasswordDialog.edtForgotEmail.hideSoftKeyboard()
                if (forgotPasswordDialog.edtForgotEmail.textToString().isEmpty()) {
                    toast("Please Enter Email")
                    return@onClick
                }
                if (!forgotPasswordDialog.edtForgotEmail.isValidEmail()) {
                    toast("Please Enter Valid Email")
                    return@onClick
                }

                val requestModel = RequestModel()
                requestModel.user_login = forgotPasswordDialog.edtForgotEmail.textToString()

                callApi(getRestApis().forgetPassword(requestModel), onApiSuccess = {
                    forgotPasswordDialog.edtForgotEmail.hideSoftKeyboard()
                    toast(it.message ?: "")
                    forgotPasswordDialog.dismiss()
                }, onApiError = {
                    toast(it)
                }, onNetworkError = {
                    toast(R.string.error_no_internet)
                })
            }

            show()
        }
    }
}