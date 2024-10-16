package com.trex.rexandroidsecureclient.search;

import com.trex.rexandroidsecureclient.R;
import com.trex.rexandroidsecureclient.common.BaseSearchablePolicyPreferenceFragment;
import com.trex.rexandroidsecureclient.comp.BindDeviceAdminFragment;
import com.trex.rexandroidsecureclient.policy.OverrideApnFragment;
import com.trex.rexandroidsecureclient.policy.PolicyManagementFragment;
import com.trex.rexandroidsecureclient.policy.keyguard.LockScreenPolicyFragment;
import com.trex.rexandroidsecureclient.policy.keyguard.PasswordConstraintsFragment;
import com.trex.rexandroidsecureclient.profilepolicy.ProfilePolicyManagementFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores all the indexable fragments.
 *
 * <p>To index a newly added fragment, there are only two things needed to be done. Make you
 * fragment extends {@link BaseSearchablePolicyPreferenceFragment} and add it to this class.
 */
public class IndexableFragments {
  private static final List<BaseIndexableFragment> sIndexableFragments = new ArrayList<>();

  static {
    sIndexableFragments.add(
        new XmlIndexableFragment(PolicyManagementFragment.class, R.xml.device_policy_header));
    sIndexableFragments.add(
        new XmlIndexableFragment(
            ProfilePolicyManagementFragment.class, R.xml.profile_policy_header));
    sIndexableFragments.add(
        new XmlIndexableFragment(LockScreenPolicyFragment.class, R.xml.lock_screen_preferences));
    sIndexableFragments.add(
        new XmlIndexableFragment(
            PasswordConstraintsFragment.class, R.xml.password_constraint_preferences));
    sIndexableFragments.add(
        new XmlIndexableFragment(BindDeviceAdminFragment.class, R.xml.bind_device_admin_policies));
    sIndexableFragments.add(new UserRestrictionIndexableFragment());
    sIndexableFragments.add(
        new XmlIndexableFragment(OverrideApnFragment.class, R.xml.override_apn_preferences));
  }

  public static List<BaseIndexableFragment> values() {
    return new ArrayList<>(sIndexableFragments);
  }
}
