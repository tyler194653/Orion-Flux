#!/usr/bin/env python3
import os
import re
import xml.etree.ElementTree as ET

ZH_PATTERN = re.compile(r"[\u4e00-\u9fff]")

def check_xml_layouts(root_dir):
    errors = []
    layout_dir = os.path.join(root_dir, "app/src/main/res/layout")
    menu_dir = os.path.join(root_dir, "app/src/main/res/menu")
    dirs_to_check = [layout_dir, menu_dir]

    for d in dirs_to_check:
        if not os.path.exists(d): continue
        for fname in os.listdir(d):
            if not fname.endswith(".xml"): continue
            fpath = os.path.join(d, fname)
            with open(fpath, "r", encoding="utf-8") as f:
                lines = f.readlines()
            for idx, line in enumerate(lines, 1):
                # Check for android:text="..." or android:hint="..." or android:title="..." with hardcoded text
                for attr in ["android:text", "android:hint", "android:title"]:
                    match = re.search(rf'{attr}="([^"]+)"', line)
                    if match:
                        val = match.group(1)
                        if not val.startswith("@string/") and not val.startswith("?attr/") and ZH_PATTERN.search(val):
                            errors.append(f"{fpath}:{idx} -> {attr}=\"{val}\" (Hardcoded text in XML)")
    return errors

def check_strings_parity(root_dir):
    errors = []
    base_strings = os.path.join(root_dir, "app/src/main/res/values/strings.xml")
    zh_strings = os.path.join(root_dir, "app/src/main/res/values-zh/strings.xml")
    en_strings = os.path.join(root_dir, "app/src/main/res/values-en/strings.xml")

    def get_keys(path):
        if not os.path.exists(path): return set()
        tree = ET.parse(path)
        root = tree.getroot()
        keys = set()
        for child in root:
            if child.tag == "string" and "name" in child.attrib:
                keys.add(child.attrib["name"])
        return keys

    base_keys = get_keys(base_strings)
    zh_keys = get_keys(zh_strings)
    en_keys = get_keys(en_strings)

    all_keys = base_keys | zh_keys | en_keys
    for k in sorted(all_keys):
        if k not in base_keys:
            errors.append(f"Missing in values/strings.xml: {k}")
        if k not in zh_keys:
            errors.append(f"Missing in values-zh/strings.xml: {k}")
        if k not in en_keys:
            errors.append(f"Missing in values-en/strings.xml: {k}")
    return errors

def check_kotlin_literals(root_dir):
    errors = []
    kt_dir = os.path.join(root_dir, "app/src/main/java")
    for root, _, files in os.walk(kt_dir):
        for fname in files:
            if not fname.endswith(".kt"): continue
            fpath = os.path.join(root, fname)
            # Skip mock repository implementations since mock data contains intentional Chinese/English
            if "Mock" in fname: continue
            with open(fpath, "r", encoding="utf-8") as f:
                lines = f.readlines()
            for idx, line in enumerate(lines, 1):
                stripped = line.strip()
                if stripped.startswith("//") or stripped.startswith("*") or stripped.startswith("/*"):
                    continue
                # check if there is a string literal with Chinese
                # string literal inside quotes
                matches = re.findall(r'"([^"\\]*(?:\\.[^"\\]*)*)"', line)
                for m in matches:
                    if ZH_PATTERN.search(m):
                        errors.append(f"{fpath}:{idx} -> \"{m}\"")
    return errors

if __name__ == "__main__":
    repo_root = os.getcwd()
    xml_errs = check_xml_layouts(repo_root)
    parity_errs = check_strings_parity(repo_root)
    kt_errs = check_kotlin_literals(repo_root)

    print(f"=== XML Layout Errors: {len(xml_errs)} ===")
    for e in xml_errs[:10]: print("  ", e)
    if len(xml_errs) > 10: print(f"  ... and {len(xml_errs)-10} more")

    print(f"\n=== Strings Parity Errors: {len(parity_errs)} ===")
    for e in parity_errs[:10]: print("  ", e)
    if len(parity_errs) > 10: print(f"  ... and {len(parity_errs)-10} more")

    print(f"\n=== Kotlin Hardcoded String Errors: {len(kt_errs)} ===")
    for e in kt_errs[:10]: print("  ", e)
    if len(kt_errs) > 10: print(f"  ... and {len(kt_errs)-10} more")
