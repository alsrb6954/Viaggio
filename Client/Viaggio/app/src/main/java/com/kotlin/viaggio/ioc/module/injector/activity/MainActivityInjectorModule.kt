@file:Suppress("unused")

package com.kotlin.viaggio.ioc.module.injector.activity

import com.kotlin.viaggio.view.camera.CameraFragment
import com.kotlin.viaggio.view.popup.BackActionDialogFragment
import com.kotlin.viaggio.view.popup.ReviewActionDialogFragment
import com.kotlin.viaggio.view.setting.*
import com.kotlin.viaggio.view.sign.SignInFragment
import com.kotlin.viaggio.view.sign.SignUpFragment
import com.kotlin.viaggio.view.theme.ThemeFragment
import com.kotlin.viaggio.view.travel.TravelFragment
import com.kotlin.viaggio.view.travel.calendar.TravelCalendarFragment
import com.kotlin.viaggio.view.travel.enroll.TravelEnrollFragment
import com.kotlin.viaggio.view.travel.kinds.TravelKindsBottomSheetDialogFragment
import com.kotlin.viaggio.view.travel.option.TravelOptionBottomSheetDialogFragment
import com.kotlin.viaggio.view.travel.option.TravelTitleBottomSheetDialogFragment
import com.kotlin.viaggio.view.travel.option.TravelingInstagramShareFragment
import com.kotlin.viaggio.view.travel.option.TravelingRepresentativeImageFragment
import com.kotlin.viaggio.view.traveling.TravelCardBottomSheetDialogFragment
import com.kotlin.viaggio.view.traveling.TravelingDeleteActionDialogFragment
import com.kotlin.viaggio.view.traveling.TravelingFinishActionDialogFragment
import com.kotlin.viaggio.view.traveling.TravelingFragment
import com.kotlin.viaggio.view.traveling.country.TravelingCityFragment
import com.kotlin.viaggio.view.traveling.country.TravelingCountryFragment
import com.kotlin.viaggio.view.traveling.country.TravelingDomesticsCountryFragment
import com.kotlin.viaggio.view.traveling.day_trip.TravelDayTripFragment
import com.kotlin.viaggio.view.traveling.detail.TravelingDetailActionDialogFragment
import com.kotlin.viaggio.view.traveling.detail.TravelingDetailFragment
import com.kotlin.viaggio.view.traveling.detail.TravelingImageDetailActionDialogFragment
import com.kotlin.viaggio.view.traveling.enroll.TravelingCardEnrollFragment
import com.kotlin.viaggio.view.traveling.enroll.TravelingCardImageEnrollFragment
import com.kotlin.viaggio.view.traveling.image.TravelCardImageModifyFragment
import com.kotlin.viaggio.view.traveling.option.TravelingCitiesActionDialogFragment
import com.kotlin.viaggio.view.traveling.option.TravelingDayCountActionDialogFragment
import com.kotlin.viaggio.view.traveling.option.TravelingThemesActionDialogFragment
import com.kotlin.viaggio.view.tutorial.TutorialFragment
import com.kotlin.viaggio.view.tutorial.intro.IntroFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface MainActivityInjectorModule {
    @ContributesAndroidInjector
    fun introFragment(): IntroFragment

    @ContributesAndroidInjector
    fun tutorialFragment(): TutorialFragment

    @ContributesAndroidInjector
    fun travelingFragment(): TravelingFragment

    @ContributesAndroidInjector
    fun traveledFragment(): TravelFragment

    @ContributesAndroidInjector
    fun settingFragment(): SettingFragment

    @ContributesAndroidInjector
    fun signInFragment(): SignInFragment

    @ContributesAndroidInjector
    fun signUpFragment(): SignUpFragment

    @ContributesAndroidInjector
    fun cameraFragment(): CameraFragment

    @ContributesAndroidInjector
    fun themeFragment(): ThemeFragment

    @ContributesAndroidInjector
    fun travelingDetailFragment(): TravelingDetailFragment

    @ContributesAndroidInjector
    fun travelingDetailActionDialogFragment(): TravelingDetailActionDialogFragment

    @ContributesAndroidInjector
    fun travelingCardEnrollFragment(): TravelingCardEnrollFragment

    @ContributesAndroidInjector
    fun travelingRepresentativeImageFragment(): TravelingRepresentativeImageFragment

    @ContributesAndroidInjector
    fun travelingFinishActionDialogFragment(): TravelingFinishActionDialogFragment

    @ContributesAndroidInjector
    fun travelingCountryFragment(): TravelingCountryFragment

    @ContributesAndroidInjector
    fun travelingCityFragment(): TravelingCityFragment

    @ContributesAndroidInjector
    fun travelEnrollFragment(): TravelEnrollFragment

    @ContributesAndroidInjector
    fun travelKindsBottomSheetDialogFragment(): TravelKindsBottomSheetDialogFragment

    @ContributesAndroidInjector
    fun travelOptionBottomSheetDialogFragment(): TravelOptionBottomSheetDialogFragment

    @ContributesAndroidInjector
    fun travelTitleBottomSheetDialogFragment(): TravelTitleBottomSheetDialogFragment

    @ContributesAndroidInjector
    fun travelingOfDayEnrollFragment(): TravelingCardImageEnrollFragment

    @ContributesAndroidInjector
    fun travelingThemesActionDialogFragment(): TravelingThemesActionDialogFragment

    @ContributesAndroidInjector
    fun travelingCitiesActionDialogFragment(): TravelingCitiesActionDialogFragment

    @ContributesAndroidInjector
    fun travelingDayCountActionDialogFragment(): TravelingDayCountActionDialogFragment

    @ContributesAndroidInjector
    fun settingImageBottomSheetDialogFragment(): SettingImageBottomSheetDialogFragment

    @ContributesAndroidInjector
    fun settingAutoBottomSheetDialogFragment(): SettingAutoBottomSheetDialogFragment

    @ContributesAndroidInjector
    fun settingMyProfileFragment(): SettingMyProfileFragment

    @ContributesAndroidInjector
    fun settingPasswordFragment(): SettingPasswordFragment

    @ContributesAndroidInjector
    fun logoutActionDialogFragment(): LogoutActionDialogFragment

    @ContributesAndroidInjector
    fun travelCardBottomSheetDialogFragment(): TravelCardBottomSheetDialogFragment

    @ContributesAndroidInjector
    fun travelingDomesticsCountryFragment(): TravelingDomesticsCountryFragment

    @ContributesAndroidInjector
    fun travelCalendarFragment(): TravelCalendarFragment

    @ContributesAndroidInjector
    fun uploadCheckActionDialogFragment(): UploadCheckActionDialogFragment

    @ContributesAndroidInjector
    fun settingProfileImageEnrollFragment(): SettingProfileImageEnrollFragment

    @ContributesAndroidInjector
    fun travelingDeleteActionDialogFragment(): TravelingDeleteActionDialogFragment

    @ContributesAndroidInjector
    fun travelingImageDetailActionDialogFragment(): TravelingImageDetailActionDialogFragment

    @ContributesAndroidInjector
    fun settingLockFragment(): SettingLockFragment

    @ContributesAndroidInjector
    fun settingLockActionDialogFragment(): SettingLockActionDialogFragment

    @ContributesAndroidInjector
    fun syncActionDialogFragment(): SyncActionDialogFragment

    @ContributesAndroidInjector
    fun travelCardImageModifyFragment(): TravelCardImageModifyFragment

    @ContributesAndroidInjector
    fun travelingInstagramShareFragment(): TravelingInstagramShareFragment

    @ContributesAndroidInjector
    fun backActionDialogFragment(): BackActionDialogFragment

    @ContributesAndroidInjector
    fun reviewActionDialogFragment(): ReviewActionDialogFragment

    @ContributesAndroidInjector
    fun travelDayTripFragment(): TravelDayTripFragment

}