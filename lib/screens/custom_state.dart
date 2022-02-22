import 'package:dropdown_button2/dropdown_button2.dart';
import 'package:flutter/material.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:get/get_rx/src/rx_types/rx_types.dart';
import 'package:get/get_state_manager/src/rx_flutter/rx_obx_widget.dart';

import '../data/option.dart';

abstract class CustomState<T extends StatefulWidget> extends State<T> {
  late AppLocalizations locale;
  late ThemeData theme;

  @override
  Widget build(BuildContext context) {
    locale = AppLocalizations.of(context)!;
    theme = Theme.of(context);
    return content();
  }

  Widget content();

  InputDecoration defaultDecoration(String labelText) {
    return InputDecoration(
      border: const OutlineInputBorder(),
      labelText: labelText,
    );
  }

  Widget expandableDropDownListItem<O>(Option<O> option, Rx<O> optionValue, String title, {Widget? child, ValueChanged<O?>? onChanged}) {
    var childWidgets = <Widget>[const Divider(), dropDownListItem(option, optionValue, onChanged: onChanged)];

    if (child != null) {
      childWidgets.add(child);
    }

    childWidgets.add(const SizedBox(height: 8));

    return expandableListItem(title: title, subtitle: () => option.asText(optionValue.value, locale), children: childWidgets);
  }

  Widget dropDownListItem<O>(Option<O> option, Rx<O> optionValue, {ValueChanged<O?>? onChanged}) {
    return Obx(() => DropdownButtonFormField2<O>(
          value: optionValue.value,
          onChanged: (O? newValue) {
            if (newValue != null) {
              optionValue.value = newValue;
            }
            if (onChanged != null) {
              onChanged(newValue);
            }
          },
          items: option.options.map<DropdownMenuItem<O>>((O value) {
            return DropdownMenuItem<O>(
              value: value,
              child: Text(option.asText(value, locale)),
            );
          }).toList(),
        ));
  }

  Widget expandableListItem({required String title, required String Function() subtitle, required List<Widget> children}) {
    Widget? subtitleWidget;
    subtitleWidget = Obx(() => Text(subtitle()));

    return ExpansionTile(
        title: Text(title),
        subtitle: subtitleWidget,
        backgroundColor: theme.colorScheme.surfaceVariant,
        textColor: theme.colorScheme.onSurfaceVariant,
        childrenPadding: const EdgeInsets.fromLTRB(8, 0, 8, 8),
        children: children);
  }

  Widget customIconButton({required Icon icon, required VoidCallback onPressed}) {
    return RawMaterialButton(
        onPressed: () {},
        clipBehavior: Clip.antiAlias,
        constraints: const BoxConstraints(minWidth: 0.0, minHeight: 0.0),
        shape: const RoundedRectangleBorder(borderRadius: BorderRadius.all(Radius.circular(5))),
        fillColor: theme.colorScheme.background,
        child: IconButton(onPressed: onPressed, icon: icon, splashColor: theme.colorScheme.tertiary));
  }
}