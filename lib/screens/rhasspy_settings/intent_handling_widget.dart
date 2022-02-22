import 'package:flutter/material.dart';
import 'package:get/get.dart';

import '../../data/intent_handling_options.dart';
import '../custom_state.dart';

extension IntentHandlingWidget on CustomState {
  Widget intentHandling() {
    var intentHandlingOption = IntentHandlingOption.disabled.obs;
    return expandableDropDownListItem(IntentHandlingOptions(), intentHandlingOption, locale.intentHandling, child: Obx(() => intentHandlingSettings(intentHandlingOption.value)));
  }

  Widget intentHandlingSettings(IntentHandlingOption intentHandlingOption) {
    var homeAssistantIntentOption = HomeAssistantIntent.events.obs;

    if (intentHandlingOption == IntentHandlingOption.remoteHTTP) {
      return Column(children: [const Divider(), TextFormField(decoration: defaultDecoration(locale.remoteURL))]);
    } else if (intentHandlingOption == IntentHandlingOption.homeAssistant) {
      return Column(children: [
        const Divider(),
        TextFormField(decoration: defaultDecoration(locale.hassURL)),
        const Divider(thickness: 0),
        TextFormField(decoration: defaultDecoration(locale.accessToken)),
        const Divider(),
        ListTile(
            title: Text(locale.homeAssistantEvents),
            leading: Obx(() => Radio<HomeAssistantIntent>(
                  value: HomeAssistantIntent.events,
                  groupValue: homeAssistantIntentOption.value,
                  onChanged: (HomeAssistantIntent? value) {
                    if (value != null) {
                      homeAssistantIntentOption.value = HomeAssistantIntent.events;
                    }
                  },
                ))),
        ListTile(
            title: Text(locale.homeAssistantIntents),
            leading: Obx(() => Radio<HomeAssistantIntent>(
                  value: HomeAssistantIntent.intents,
                  groupValue: homeAssistantIntentOption.value,
                  onChanged: (HomeAssistantIntent? value) {
                    if (value != null) {
                      homeAssistantIntentOption.value = HomeAssistantIntent.intents;
                    }
                  },
                ))),
      ]);
    } else {
      return Container();
    }
  }
}