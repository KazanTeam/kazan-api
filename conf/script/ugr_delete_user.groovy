"{
        ""firebase_toke"":""firebase_token"",
        ""group_id"":1, 
        ""deleted_user_id"":""67246a71-c66d-11e9-a75d-42010a4db004""
}"	"{
    ""http_code"": ""202""
}

{
    ""http_code"": ""404"",
    ""error"": ""No user with this user_id or user has registered!!!""
}"	"Điều kiện: request user = group.creater & key( user_id, group_id) tìm thấy user_group_role
Delete user_group_role.role_id theo key (roup_id & user_id)"