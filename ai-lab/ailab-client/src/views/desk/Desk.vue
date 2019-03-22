<template>
    <section>
        <admin-desk v-if="userType===1"></admin-desk>
        <!--
        <user-desk v-else></user-desk>
        -->
    </section>
</template>
<script>
import AdminDesk from '@/components/desk/AdminDesk';

export default {
    components: { AdminDesk },
    data() {
        return {
           userType: 1
        }
    },
    methods: {
        
    },
    mounted() {
        let { userType } = JSON.parse(sessionStorage.getItem("user"));
        this.userType = userType;
        console.log(this.userType);
    }
}
</script>

<style scoped>

</style>