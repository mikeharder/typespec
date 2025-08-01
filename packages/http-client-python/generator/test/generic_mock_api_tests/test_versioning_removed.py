# -------------------------------------------------------------------------
# Copyright (c) Microsoft Corporation. All rights reserved.
# Licensed under the MIT License. See License.txt in the project root for
# license information.
# --------------------------------------------------------------------------
import pytest
from versioning.removed import RemovedClient
from versioning.removed.models import ModelV2, EnumV2


@pytest.fixture
def client():
    with RemovedClient(endpoint="http://localhost:3000", version="v2") as client:
        yield client


def test_v2(client: RemovedClient):
    assert client.v2(ModelV2(prop="foo", enum_prop=EnumV2.ENUM_MEMBER_V2, union_prop="bar")) == ModelV2(
        prop="foo", enum_prop=EnumV2.ENUM_MEMBER_V2, union_prop="bar"
    )


def test_model_v3(client: RemovedClient):
    result = client.model_v3({"id": "123", "enumProp": "enumMemberV1"})
    assert result.id == "123"
    assert result.enum_prop == "enumMemberV1"
