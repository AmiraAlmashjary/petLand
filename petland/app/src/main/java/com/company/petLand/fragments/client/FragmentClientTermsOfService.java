package com.company.petLand.fragments.client;

import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.company.petLand.R;
import com.company.petLand.databinding.FragmentClientTermsBinding;


public class FragmentClientTermsOfService extends Fragment {

    private FragmentClientTermsBinding binding;


    public FragmentClientTermsOfService() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentClientTermsBinding.inflate(LayoutInflater.from(container.getContext()), container, false);

        binding.textView17.setText(Html.fromHtml("This privacy policy statement explains how Pet Land uses and protects any information collected when you use this app. It is committed to ensuring that your privacy and information are protected. You will be asked to provide certain personally identifiable information when using this application and this information will only be used in accordance with the privacy policy statement. Note that the privacy policy may change from time to time by updating this page. You should review this page from time to time to ensure that these changes are appropriate.\n" +
                "\n" +
                "What information is collected?\n" +
                "We may collect the following information:\n" +
                "•\tThe name\n" +
                "• Contact information including email address\n" +
                "• Demographic information such as zip code، favorite merchandise and your interests\n" +
                "• Other information relevant to customer surveys and/or offers\n" +
                " \n" +
                "How the collected information is used\n" +
                "We need this information to understand your needs and to provide a better service, and in particular for the following reasons:\n" +
                "• Internal record keeping We use this information to improve our products and services.\n" +
                "• We may periodically send promotional emails about new products، special offers or other information which we think may be of interest to you using the email address.\n" +
                "• We may use your information to contact you for marketing research purposes. You will be contacted by e-mail, phone, or mail.\n" +
                "• We may use this information to customize the site according to your interests.\n" +
                " \n" +
                "Information protection\n" +
                "We are committed to ensuring that your private information is protected from being accessed or disclosed by any unauthorized party, and we have put in place all necessary procedures in the field and electronically to protect and secure the information collected on our site.\n" +
                "Sharing your information with third parties\n" +
                "We do not trade, sell, distribute or rent your personal information to third parties unless we have your consent or are required by law to do so. We use your personal information to send you information about offers and updates in our store.\n" +
                "\n" +
                "Geographical location\n" +
                "The User may at times be required to consent to the User's geolocation in order to benefit from some of the services offered on the Pet Kennel Store website and application.\n" +
                "Linking to other websites\n" +
                "Our store may provide links to third-party websites for your convenience, and by using these links, you will leave our store and we do not control those sites or their legal practices, which differ from our store approach and we do not support third-party websites, nor do we represent them. These terms and conditions do not they apply to other websites, and we invite you to review the terms and conditions of any other website before entering the same website.\n" +
                "\n" +
                "When you contact us\n" +
                "All data provided by you will be treated as confidential. Forms that are submitted directly on the network require the provision of data that will help us improve our site. The data provided by you will be used to respond to all your inquiries, comments, or requests by this site or any of its affiliate sites."));
        binding.textView17.setMovementMethod(new ScrollingMovementMethod());

        return binding.getRoot();
    }



}